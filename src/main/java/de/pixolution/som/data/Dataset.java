package de.pixolution.som.data;

import de.pixolution.som.math.DistanceFunction;

public interface Dataset {
	
	/**
	 * Get the data set
	 * 
	 * @return
	 */
	public EmbeddingData[] getAll();
		
	/**
	 * Comparison function for different feature vectors of data set entries
	 * 
	 * @return 
	 */
	public DistanceFunction<float[]> getFeatureDistanceFunction();

}
