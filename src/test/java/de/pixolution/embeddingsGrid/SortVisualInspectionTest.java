package de.pixolution.embeddingsGrid;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.directory.InvalidAttributesException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import de.pixolution.embeddingGrid.dataset.CSVReaderDataset;
import de.pixolution.embeddingsGrid.invoker.OpenApiGeneratorApplication;
import de.pixolution.som.data.EmbeddingData;
import de.pixolution.som.data.Grid;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, 
classes = OpenApiGeneratorApplication.class)
@AutoConfigureMockMvc
public class SortVisualInspectionTest {

	@Autowired
	private MockMvc mvc;
	private final String dataCsv = "src/test/resources/blm_images_list.csv";
	private Map<String, String> uidToUriMap = null;
	private final String imgUrlTpl = "https://expotest.bsz-bw.de/blm/digitaler-katalog/image?id=%s&width=300";
	
	/**
	 * Load the embedding data from the CSV file, sort the embeddings using the MockMVC API, 
	 * visualize response using the GridHtmlWriter class. Outputs a HTML file to open in browser afterwards.
	 * @throws Exception
	 */
	@Test
	public void visualInspectionHtmlTest() throws Exception {
		int numElements = 1000;
		int[][] seeds = new int[][] {
			{3, 3},
			null,
			null,
			{20, 2},
			null,
			null,
			null,
			{20, 20},
			null,
			null
		};
		int[][] sizes = new int[][] {
			{2, 2},
			null,
			null,
			{2, 2},
			null,
			null,
			null,
			{2, 2},
			null,
			null
		};
		byte[] testJson = getRequestBody(dataCsv, 10, 10, false, numElements, seeds, sizes);		
		System.out.println(new String(testJson));
		MvcResult result = mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		//.andDo(MockMvcResultHandlers.print())
		.andReturn();
		// convert response to JSONObject
		String resultBody = result.getResponse().getContentAsString();
		JSONObject json = new JSONObject(resultBody);
		writeToFile(json.toString(2), "examples/response.json");
		// reconstruct the Grid object from response
		JSONArray imagesJson = json.getJSONArray("sorted_images");
		System.out.println("Num images in JSON response: "+imagesJson.length());
		int columns = json.getJSONObject("grid").getInt("columns");
		int rows = json.getJSONObject("grid").getInt("rows");
		EmbeddingData[] images = new EmbeddingData[rows*columns];
		Grid sortedGrid = new Grid(columns, rows, images);
		// place the images properly		
		int numImgs = imagesJson.length();
		for (int i=0;i<numImgs;i+=1) {
			JSONObject img = imagesJson.getJSONObject(i);
			int col = img.getInt("column");
			int row = img.getInt("row");
			if (sortedGrid.getElement(col, row) != null) throw new Error("Grid slot already taken: "+sortedGrid.getElement(col, row).toString());
			EmbeddingData ed = new EmbeddingData(img.getString("id"), null);
			int size_cols = img.getInt("size_cols");
			int size_rows = img.getInt("size_rows");
			if (size_cols>1 || size_rows>1) {
				ed.setSize(size_cols, size_rows);
			}
			sortedGrid.setElement(col, row, ed);
		}
		// render to HTML view
		GridHtmlView htmlRenderer = new GridHtmlView(sortedGrid, uidToUriMap, "examples/html_urls/BLM_sorted_color_shape.html", null);

	}

	/**
	 * Load a CSV file with line format "id, embedding_base64, file_path" and return the SortingRequestImagesInner[]
	 * produced by requests.
	 *
	 * @param csvFile
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws InvalidAttributesException
	 */
	protected byte[] getRequestBody(String csvFile, int columns, int rows, boolean doWrap, int numToUse, int[][] seeds, int[][] sizes) throws JSONException, IOException, InvalidAttributesException {
		uidToUriMap = new TreeMap<>();
		// create image list
		JSONArray images = new JSONArray();
		CSVReaderDataset dataset = new CSVReaderDataset(csvFile);
		EmbeddingData[] csvItems = dataset.getAll();
		if (numToUse<=0) numToUse = csvItems.length;
		for (int a=0;a<numToUse;a+=1) {
			// create the image object
			JSONObject img = new JSONObject();
			img.put("id", csvItems[a].getId());
			uidToUriMap.put(csvItems[a].getId(), String.format(imgUrlTpl, csvItems[a].getId()));
			String base64Embedding = Base64.getEncoder().encodeToString(EmbeddingData.floatToByteArray(csvItems[a].getFeature()));
			img.put("embedding", base64Embedding);
			// seeds
			if (seeds != null && seeds.length > a && seeds[a] != null) {
				img.put("seed_column", seeds[a][0]);
				img.put("seed_row", seeds[a][1]);
			}
			// sizes
			if (sizes != null && seeds.length > a && sizes[a] != null) {
				img.put("size_cols", sizes[a][0]);
				img.put("size_rows", sizes[a][1]);
			}
			// add to array
			images.put(img);
		}
		// create request body with images and grid definition
		JSONObject requestBody = new JSONObject();
		requestBody.put("images", images);
		JSONObject grid = new JSONObject();
		grid.put("aspect_ratio", 1.0f);
		grid.put("size_factor", 17);
		grid.put("wrapped_mode", true);
		requestBody.put("grid", grid);
		// print request body
		String requestBodyStr = requestBody.toString(2);
		writeToFile(requestBodyStr, "examples/request.json");
		//System.out.println(requestBodyStr);
		// return bytes to include into POST request body
		return requestBodyStr.getBytes();
	}
	
    public void writeToFile(String content, String path) {
    	System.out.println("Write text into "+path);
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
	
}
