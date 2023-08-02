package de.pixolution.som.data;

import java.util.Arrays;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import de.pixolution.embeddingsGrid.model.SortingRequestImagesInnerJson;
import de.pixolution.som.math.DistanceFunction;
/**
 * Convert the JSON request model object to a data structure that the FLAS/LAS implementation
 * can deal with. This class also takes care that we have enough slots for the over-sized-elements
 * by adding space for them as needed.
 */
public class RequestDataset implements Dataset {

	protected EmbeddingData[] images;
	
	public RequestDataset(List<SortingRequestImagesInnerJson> images) throws InvalidAttributesException {
		this.images = images.stream()
				.map(EmbeddingData::new)
				.toArray(EmbeddingData[]::new);
		enlargeImageArrayForLargeElements();
	}

	public RequestDataset(EmbeddingData[] images) throws InvalidAttributesException {
		this.images = images;
		enlargeImageArrayForLargeElements();
	}
	/**
	 * Elements that are fixed can have a size > 1x1. If there are elements larger
	 * than one slot they need to be added multiple times fixed on the area it covers.
	 * This method calculates the number additional slots needed and enlarge the images 
	 * array class attribute to make space for them. It does not add and place the 
	 * element, this needs to be done after grid size has been calculated out of the specs.
	 * @throws InvalidAttributesException 
	 */
	protected void enlargeImageArrayForLargeElements() throws InvalidAttributesException {
		EmbeddingData ed = null;
		int numAdditionalSlotsNeeded = 0;
		// calculate how many additional slots we need
		for (int i=0;i<this.images.length;i+=1) {
			ed = this.images[i];
			if (ed.isLargerThanOneSlot()) {
				// check if the element is fixed. If not throw unexpected error
				if (! ed.isSeeded()) {
					String msg = "The element "+ed.id+" has a size of "+ed.sizeColumns+"x"+ed.sizeRows+" (larger that 1x1) and it is not fixed. This is not allowed.";
					throw new InvalidAttributesException(msg);
				}
				// calculate the needed slots for this element and sum up
				numAdditionalSlotsNeeded += ed.sizeColumns*ed.sizeRows - 1; 
			}
		}
		// now copy and enlarge the array of images to make space for the additional items
		this.images = Arrays.copyOf(this.images, this.images.length + numAdditionalSlotsNeeded);
	}
	
	@Override
	public EmbeddingData[] getAll() {
		return images;
	}

	@Override
	public DistanceFunction<float[]> getFeatureDistanceFunction() {
		return (float[] element1, float[] element2) -> {
			return DistanceFunction.getL2Distance(element1, element2);
		};
	}

}
