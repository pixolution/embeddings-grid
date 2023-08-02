package de.pixolution.embeddingsGrid.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.naming.directory.InvalidAttributesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import de.pixolution.embeddingsGrid.model.SortingRequestGridJson;
import de.pixolution.embeddingsGrid.model.SortingRequestImagesInnerJson;
import de.pixolution.embeddingsGrid.model.SortingRequestJson;
import de.pixolution.som.data.Dataset;
import de.pixolution.som.data.EmbeddingData;
import de.pixolution.som.data.Grid;
import de.pixolution.som.data.RequestDataset;
import de.pixolution.som.math.FLAS;
import de.pixolution.som.math.SorterInterface;
/**
 * Create a ArrangeGridRequest that calculates the sorted grid and provides the result. This
 * class is the glue code between the LAS/FLAS implementation and the spring 5 controller
 * class.
 * @author Sebastian Lutter, pixolution.org
 */
public class ArrangeGridRequest {
	
	protected long randomSeed;
	protected boolean hasSeed;
	protected Dataset dataset;
	protected boolean wrappedMode;
	protected int columns, rows;
	private final Logger log = LoggerFactory.getLogger(ArrangeGridRequest.class);

	/**
	 * 
	 * Create an arrange request with fixed columns and rows. This constructor does not prepare
	 * the seeded elements or elements that have a size attribute set. It expects that the large
	 * sized elements are already unwrapped (added multiple times to occupy the space) and that
	 * fixed elements are already in the proper position. 
	 * @param request
	 * @throws InvalidAttributesException 
	 */
	public ArrangeGridRequest(SortingRequestJson request) throws InvalidAttributesException {
		// get the list of images
		List<SortingRequestImagesInnerJson> images = request.getImages();
		SortingRequestGridJson reqGrid = request.getGrid();
		// Convert the request data into EmbeddingData[] for further processing.
		// This step also enlarges the image array to add needed slots for large-sized elements (> 1x1)
		this.dataset = new RequestDataset(images);
		// calculate 0-100 percentage value to a float: 17% --> 1.17
		float sizeFactor = (request.getGrid().getSizeFactor().intValue()/100.0f) + 1.0f;
		float aspectRatio = request.getGrid().getAspectRatio().floatValue();
		// Calculate the grid to fit the spec.
		// The needed slots for the large-sized elements have been already added, but elements
		// are not placed yet.
		int[] dims = calculateGridDimensions(aspectRatio, sizeFactor, this.dataset.getAll().length);
		this.columns = dims[0];
		this.rows = dims[1];
		// should wrapped mode be used?
		this.wrappedMode = reqGrid.getWrappedMode();
		// check if all seeds are within bounds of the grid
		checkSeedsAreWithinGrid(this.dataset.getAll(), null);
	}
	
	/**
	 * Create an arrange request with fixed columns and rows. This constructor does not prepare
	 * the seeded elements or elements that have a size attribute set. It expects that the large
	 * sized elements are already unwrapped (added multiple times to occupy the space) and that
	 * fixed elements are already in the proper position. 
	 * @param dataset The dataset with the list of elements to place on the grid
	 * @param columns The number of columns of the resulting grid
	 * @param rows The number of rows of the resulting grid
	 * @param wrappedMode Should the grid be calculated in infinity wrapped mode?
	 */
	public ArrangeGridRequest(Dataset dataset, int columns, int rows, boolean wrappedMode) {
		this.dataset = dataset;
		this.columns = columns;
		this.rows = rows;
		this.wrappedMode = wrappedMode;
	}
		
	public void seedRandom(long seed) {
		this.randomSeed = seed;
		this.hasSeed = true;
	}
	
	/**
	 * Arrange the given list of images on the grid using FLAS/LAS algorithm. Takes care of
	 * seeded and over-sized elements placement. Will throw an ApiError in case of invalid 
	 * attributes or misplaced seeds.
	 * @return
	 */
	public Grid arrange() {
		final Random rndShuffle, rnd;
		if (this.hasSeed) {			
			rndShuffle = new Random(randomSeed);
			rnd = new Random(randomSeed);
		} else {
			rndShuffle = new Random();
			rnd = new Random();
		}
		// Enlarge list if needed, then arrangement starts with a random shuffle of the list of images 
		List<EmbeddingData> edList = Arrays.asList(Arrays.copyOf(dataset.getAll(), rows*columns));
		// shuffle the data in-place
		Collections.shuffle(edList, rndShuffle);
		// write back into array
		EmbeddingData[] elements = edList.toArray(new EmbeddingData[rows*columns]);
		// create the grid
		final Grid imageGrid = new Grid(columns, rows, elements);
		// restore seeded elements after shuffling before unwrapping oversized-elements
		for (int col=0;col<columns;col+=1) {
			for (int row=0;row<rows;row+=1) {
				EmbeddingData ed = imageGrid.getElement(col, row);
				if (ed != null && ed.isSeeded()) {
					// check if this slot is already taken
					EmbeddingData formerTenant = imageGrid.getElement(col, row);
					if (ed.getColumn() != col || ed.getRow() != row) {
						// place the fixed element onto the right position on the grid
						imageGrid.setElement(ed.getColumn(), ed.getRow(), ed);
						// null its previous position
						imageGrid.setElement(col, row, null);
					}
					// and give the former tenant (if any) the next empty slot in the grid 
					if (formerTenant != null && formerTenant != ed) {
						int nextUnusedIndex = getNextUnusedSlotIndex(imageGrid); 
						imageGrid.setElement(nextUnusedIndex, formerTenant);
					}
				}
			}
		}
		// Make sure the fixed elements have the right position in the array given the grid size (fix by swapping if not).
		for (int index=0;index<elements.length;index+=1) {
			EmbeddingData ed = elements[index];
			if (ed != null && ed.isSeeded() && ! ed.isClone()) {
				// do the placement with regard to the element size
				for (int colDelta=0;colDelta<ed.getSizeColumns(); colDelta+=1) {
					for (int rowDelta=0;rowDelta<ed.getSizeRows(); rowDelta+=1) {
						// calculate the slot on the grid that the element should be seeded
						int col=ed.getColumn()+colDelta;
						int row=ed.getRow()+rowDelta;
						// place a unwrap-clone
						if (colDelta==0 && rowDelta==0) continue;
						// create the unwrap clone to place
						EmbeddingData edToPlace = ed.unwrapClone(col, row);
						// check if we are within bounds of the grid, throw ApiError if not
						checkSeedsAreWithinGrid(new EmbeddingData[] {edToPlace}, "Overflowing grid while unwrapping oversized element");
						// check if this slot is already taken
						EmbeddingData formerTenant = imageGrid.getElement(col, row);
						// place the fixed element onto the right position on the grid
						imageGrid.setElement(col, row, edToPlace);
						// and give the former tenant (if any) the next empty slot in the grid 
						if (formerTenant != null) {
							int nextUnusedIndex = getNextUnusedSlotIndex(imageGrid); 
							imageGrid.setElement(nextUnusedIndex, formerTenant);
						}
					}
				}
			}
		}
		// sort the shuffled grid
		final SorterInterface ag = new FLAS(dataset.getFeatureDistanceFunction(), rnd, wrappedMode);
		long start = System.currentTimeMillis();
		Grid sortedGrid = ag.arrange(imageGrid);
		final long sortingTime = System.currentTimeMillis() - start;
		// last thing is to set the col and rows values in the EmbeddingData objects since sorting took place 
		// in the array itself.
		for (int col=0;col<sortedGrid.getColumns();col+=1) {
			for (int row=0;row<sortedGrid.getRows();row+=1) {
				EmbeddingData ed = sortedGrid.getElement(col, row);
				// set the end position after sorting in the grid as coords into the object
				if (ed != null) ed.seedOnGrid(col, row);
			}
		}
		if (elements.length>0) {
			// get the embedding dim from the first element
			EmbeddingData ed = null;
			for (int a=0;a<this.dataset.getAll().length;a+=1) {
				ed = this.dataset.getAll()[a];
				if (ed!=null) {
					break;
				}
			}
			// log the arrangement
			log.info("Arrange "+elements.length+" embeddings with "+ed.getFeature().length+" dims on "+sortedGrid.getColumns()+"x"+sortedGrid.getRows()+" grid took "+sortingTime+"ms");
		}
		return sortedGrid;
	}
	/**
	 * After unwrapping the oversized-elements it can happen that the resulting seed coordinates 
	 * are out of bound with the grid columns and/or rows. This method checks if this is the case.
	 * @param images
	 * @throws ApiError if problems are detected
	 */
	protected void checkSeedsAreWithinGrid(EmbeddingData[] images, String errorMessageAddition) {
		List<String> overflowingSeeds = new LinkedList<>();
		if (errorMessageAddition != null) overflowingSeeds.add(errorMessageAddition);
		boolean error = false;
		for (EmbeddingData img:images) {
			if (img==null || ! img.isSeeded()) continue;
			if (img.getColumn() >= this.columns || img.getRow() >= this.rows) {
				int betterCol=(img.getColumn() + img.getSizeColumns() - 1) >= this.columns ? this.columns - img.getSizeColumns() + 1 : img.getColumn();
				int betterRow=(img.getRow() + img.getSizeRows() -1) >= this.rows ? this.rows - img.getSizeRows() + 1 : img.getRow();
				overflowingSeeds.add("element "+img.getId()+": seed_column, seed_row is ("+img.getColumn()+", "+img.getRow()+") size_cols, size_rows is ("+img.getSizeColumns()+", "+img.getSizeRows()+") but grid dims are ("+this.columns+", "+this.rows+
						"). Please adjust its seed position to ("+betterCol+", "+betterRow+") and/or set the size to (1, 1)");
				error = true;
			}
		}
		// throw error if we have overflows
		if (error) throw new ApiError(HttpStatus.BAD_REQUEST, "Overflowing grid while unwrapping oversized element. "+String.join(", ", overflowingSeeds), "/sort");
	}

	protected int getNextUnusedSlotIndex(Grid grid) {
		EmbeddingData[] elements = grid.getElements();
		for (int index=0;index<elements.length;index+=1) {
			if (elements[index] == null) return index;
		}
		throw new Error("No free slot found in grid while enlarge elements are unwrapped. That means the needed space for the slots while unwrap have not been added in the step before. This is a bug.");
	}
	
	/**
	 * Calculate the number of columns and rows needed out of size factor, 
	 * aspect ratio and the number of images to place.
	 * @param aspectRatio
	 * @param sizeFactor
	 * @param numberImages
	 * @return
	 */
	private int[] calculateGridDimensions(float aspectRatio, float sizeFactor, int numberImages) {
		// effective grid size calculation
		int numSomElements = (int) Math.ceil(numberImages * sizeFactor);
		// calculate optimal number of mapPlaces 
		double thumbSize = Math.sqrt(aspectRatio / numSomElements);
		int mapPlacesX0 = (int)(aspectRatio / thumbSize); 
		int mapPlacesY0 = (int)(1. / thumbSize); 
		while(mapPlacesX0*mapPlacesY0 < numSomElements) {
			int prodXp1 = (mapPlacesX0+1)*mapPlacesY0;
			int prodYp1 = mapPlacesX0*(mapPlacesY0+1);
			// next step will solve the issue
			if (prodYp1 >= numSomElements || prodXp1 >= numSomElements) {
				if (prodYp1 >= numSomElements && prodXp1 < numSomElements) {
					mapPlacesY0++;
				} else if (prodXp1 >= numSomElements && prodYp1 < numSomElements) {
					mapPlacesX0++;
				} else {
					double aspectXp1 = aspectRatio/(mapPlacesX0+1)/(1./(mapPlacesY0));
					double aspectYp1 = aspectRatio/(mapPlacesX0)/(1./(mapPlacesY0+1));
					if(Math.abs(1-aspectXp1) < Math.abs(1-aspectYp1)) {
						mapPlacesX0++;
					} else { 
						mapPlacesY0++;
					}
				}
			} else {
				// not solved yet? go in direction of square-cells to solve
				double aspectXp1 = aspectRatio/(mapPlacesX0+1)/(1./(mapPlacesY0));
				double aspectYp1 = aspectRatio/(mapPlacesX0)/(1./(mapPlacesY0+1));
				if(Math.abs(1-aspectXp1) < Math.abs(1-aspectYp1)) {
					mapPlacesX0++;
				} else { 
					mapPlacesY0++;
				}
			}
		}
		return new int[] {mapPlacesX0, mapPlacesY0};
	}
	
	public int getGridColumns() {
		return this.columns;
	}
	public int getGridRows() {
		return this.rows;
	}
}
