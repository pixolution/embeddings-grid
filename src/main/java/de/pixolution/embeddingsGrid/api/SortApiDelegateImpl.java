package de.pixolution.embeddingsGrid.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import de.pixolution.embeddingsGrid.model.SortingRequestImagesInnerJson;
import de.pixolution.embeddingsGrid.model.SortingRequestJson;
import de.pixolution.embeddingsGrid.model.SortingResponseGridJson;
import de.pixolution.embeddingsGrid.model.SortingResponseJson;
import de.pixolution.embeddingsGrid.model.SortingResponseSortedImagesInnerJson;
import de.pixolution.som.data.EmbeddingData;
import de.pixolution.som.data.Grid;

@Component
public class SortApiDelegateImpl implements SortApiDelegate {
	

	@Autowired
	public SortApiDelegateImpl() {
		
	}
	
	@Override
	public ResponseEntity<SortingResponseJson> sortPost(SortingRequestJson sortingRequestJson) {
		// validate the inputs
		if (sortingRequestJson == null) throw new ApiError(HttpStatus.BAD_REQUEST, "Request POST body is missing, got an empty body. Expected to get a valid json request body", "/sort");
		if (sortingRequestJson.getImages() == null || sortingRequestJson.getImages().isEmpty()) {
			throw new ApiError(HttpStatus.BAD_REQUEST, "Required parameter \"images\" is missing. The parameter must not be null and should not be an emtpy array", "/sort");
		}
		// check the seeds for collisions, throw BAD_REQUEST if any are found
		Map<SortingRequestImagesInnerJson, SortingRequestImagesInnerJson> collidingPairs = findCollisions(sortingRequestJson.getImages());
		if (!collidingPairs.isEmpty()) {
			// throw a BAD_REQUEST with the conflicting seeds and the corresponding element ids
        	int num = collidingPairs.size();
        	StringBuilder msg = new StringBuilder("Found "+num+" collision"+(num>1?"s":"")+" in the elements seed coordinates: ");
        	collidingPairs.forEach((key, value) -> msg.append(String.format("id=%s and id=%s collide on col=%d row=%d), ", 
			        			                                             key.getId(), value.getId(), 
			        			                                             value.getSeedColumn(), value.getSeedRow())));
        	throw new ApiError(HttpStatus.BAD_REQUEST, msg.substring(0, msg.length() -2), "/sort");
		}
		try {
			// process the request, arrange all images on the grid
			ArrangeGridRequest arrangeTask = new ArrangeGridRequest(sortingRequestJson);
			Grid sortedGrid = arrangeTask.arrange();
			// package answer into response format
			SortingResponseJson responseJson = new SortingResponseJson();
			List<SortingResponseSortedImagesInnerJson> images = Arrays
					.stream(sortedGrid.getElements())
					.filter(Objects::nonNull)
					.map(this::convertData)
					.toList();
			responseJson.setSortedImages(images);
			responseJson.setGrid(new SortingResponseGridJson(arrangeTask.getGridColumns(), arrangeTask.getGridRows()));
			return new ResponseEntity<SortingResponseJson>(responseJson, HttpStatus.OK);
		} catch (ApiError e) {
			throw e;
		} catch (InvalidAttributesException e) {
			e.printStackTrace();
			StringBuilder reason = new StringBuilder("Invalid Attributes Error: "+e.getMessage());
			throw new ApiError(HttpStatus.BAD_REQUEST, reason.toString(), "/sort");
		} catch (Throwable e) {
			e.printStackTrace();
			String reason = "Unexpected server error: "+e.getMessage();
			throw new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, reason, "/sort");
		}
	}
	
    /**
     * Finds colliding elements and returns them as a Map of key-value pairs where collisions occur
     * (multiple objects have the same seedColumn and seedRow values).
     *
     * @param images The list of SortingRequestImagesInnerJson objects to process.
     * @return A Map<SortingRequestImagesInnerJson, SortingRequestImagesInnerJson>
     *         where each key-value pair represents two colliding elements.
     */
    public static Map<SortingRequestImagesInnerJson, SortingRequestImagesInnerJson> findCollisions(List<SortingRequestImagesInnerJson> images) {
        Map<String, List<SortingRequestImagesInnerJson>> indexMap = images.stream()
        		.filter( el -> el.getSeedColumn() >= 0 && el.getSeedRow() >= 0)
                .collect(Collectors.groupingBy(
                        image -> image.getSeedColumn() + "," + image.getSeedRow(),
                        Collectors.toList()
                ));
        // filter down to collisions and return the colliding pairs
        return indexMap.values().stream()
                .filter(list -> list.size() > 1)
                .flatMap(list -> {
                    List<SortingRequestImagesInnerJson> collidingImages = list.stream()
                            .sorted(Comparator.comparing(SortingRequestImagesInnerJson::getId))
                            .collect(Collectors.toList());
                    List<Map.Entry<SortingRequestImagesInnerJson, SortingRequestImagesInnerJson>> pairs = new ArrayList<>();
                    for (int i = 0; i < collidingImages.size(); i++) {
                        for (int j = i + 1; j < collidingImages.size(); j++) {
                            pairs.add(Map.entry(collidingImages.get(i), collidingImages.get(j)));
                        }
                    }
                    return pairs.stream();
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing
                ));
    }
    
	/**
	 * Convert a EmbeddingData element from the grid calculation to a spring model SortingResponseSortedImagesInnerJson 
	 * object.
	 * @param data
	 * @return
	 */
	protected SortingResponseSortedImagesInnerJson convertData(EmbeddingData data) {
		SortingResponseSortedImagesInnerJson converted = new SortingResponseSortedImagesInnerJson();
		converted.setId(data.getId());
		converted.setColumn(data.getColumn());
		converted.setRow(data.getRow());
		converted.setSizeCols(data.getSizeColumns());
		converted.setSizeRows(data.getSizeRows());
		return converted;
	}
}
