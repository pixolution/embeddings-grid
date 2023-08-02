package de.pixolution.embeddingGrid.dataset;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import de.pixolution.som.data.EmbeddingData;

public class CSVReaderDataset extends DatasetWithPaths {
	
	/**
	 * Read and use dataset from CSV. Expect "id, base_embedding, source file path" per line
	 * @param csvfile Path to csv file
	 * @throws IOException
	 * @throws InvalidAttributesException 
	 */
	public CSVReaderDataset(String csvfile) throws IOException, InvalidAttributesException {
		super(new EmbeddingData[0]);
		long start = System.currentTimeMillis();
		List<EmbeddingData> embeddings = new LinkedList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(csvfile))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        float[] embedding = EmbeddingData.byteToFloatArray(Base64.getDecoder().decode(values[1]));
		        embeddings.add(new EmbeddingData(values[0], embedding));
		        if (values.length>=3) {
			        // build up map with uid to source file mapping
			        uidToFileMap.put(values[0], values[2]);
		        }
		    }
		}
		this.images = embeddings.toArray(new EmbeddingData[0]);
		final long loadingTime = System.currentTimeMillis() - start;
		System.out.println("Loaded dataset from "+csvfile+" with "+this.images.length+" entries. Took "+loadingTime+"ms.");
	}

}
