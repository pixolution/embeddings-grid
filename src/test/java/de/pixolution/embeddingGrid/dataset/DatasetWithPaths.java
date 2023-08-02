package de.pixolution.embeddingGrid.dataset;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.directory.InvalidAttributesException;

import de.pixolution.som.data.EmbeddingData;
import de.pixolution.som.data.RequestDataset;

public class DatasetWithPaths extends RequestDataset {

	protected Map<String, String> uidToFileMap = new TreeMap<>();
	
	public DatasetWithPaths(EmbeddingData[] images) throws InvalidAttributesException {
		super(images);
	}


	public Map<String, String> getUidToPathMap() {
		return this.uidToFileMap;
	}
	
	public void writeToCSVFile(String outfile) {
	    File csvOutputFile = new File(outfile);
	    try (PrintWriter pw = new PrintWriter(outfile)) {
	        Arrays.stream(this.images)
	        		// write out: id, bsae64_embedding, file path
	               .map( emb -> new String[] {emb.getId(),floatsToBase64(emb.getFeature()), uidToFileMap.get(emb.getId())})
	               .map(this::convertToCSV)
	          .forEach(pw::println);
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    assertTrue(csvOutputFile.exists());
	}
	
	/**
	 * Used to load values from CSV
	 * @param base64
	 * @return
	 */
	protected float[] base64ToFloats(String base64) {
		return EmbeddingData.byteToFloatArray(Base64.getDecoder().decode(base64));
	}
	/**
	 * Used to wrap embedding to write to CSV
	 * @return
	 */
	protected String floatsToBase64(float[] embedding) {
		return Base64.getEncoder().encodeToString(EmbeddingData.floatToByteArray(embedding));
	}
	/**
	 * Turn a String[] into a CSV line
	 * @param data
	 * @return
	 */
	protected String convertToCSV(String[] data) {
		return String.join(",", data);
	    //return Stream.of(data).collect(Collectors.joining(","));
	}
}
