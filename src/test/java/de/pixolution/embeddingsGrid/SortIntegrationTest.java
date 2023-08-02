package de.pixolution.embeddingsGrid;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.naming.directory.InvalidAttributesException;

import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import de.pixolution.embeddingGrid.dataset.CSVReaderDataset;
import de.pixolution.embeddingsGrid.invoker.OpenApiGeneratorApplication;
import de.pixolution.som.data.EmbeddingData;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, 
classes = OpenApiGeneratorApplication.class)
@AutoConfigureMockMvc
public class SortIntegrationTest {

	@Autowired
	private MockMvc mvc;
	/**
	 * Random seed to send in requests as part of the grid definition
	 */
	private final long randomSeed = 1337;
	private static final String dataCsv = "src/test/resources/blm_images_list.csv";

	@Test
	public void missingPostBodyTest() throws Exception {
		String testJson = "";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("POST body is missing")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("got an empty body")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}

	@Test
	public void simpleTest() throws Exception {
		String testJson = """
							{
				  "images": [
				    {
				      "id": "408468fd-ce11",
				      "embedding": "ZGM2NmFhMDgtZmFmYS00YzZmLWFjNWYtYzRmMmI5N2Q2OTE1Cg=="
				    }
				  ],
				  "grid": {
				    "aspect_ratio": 1.0,
				    "wrapped_mode": true,
				    "size_factor": 0
				  }
				}
							""";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.jsonPath("$.grid.columns").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.grid.rows").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.sorted_images").isArray());
	}

	@Test
	public void simpleSize2x2Test() throws Exception {
		String testJson = """
							{
				  "images": [
				    {
				      "id": "408468fd-ce11",
				      "embedding": "ZGM2NmFhMDgtZmFmYS00YzZmLWFjNWYtYzRmMmI5N2Q2OTE1Cg==",
				      "size_cols": 2,
				      "size_rows": 2,
				      "seed_column": 0,
				      "seed_row": 0
				    }
				  ],
				  "grid": {
				    "aspect_ratio": 1.0,
				    "wrapped_mode": true,
				    "size_factor": 0
				  }
				}
							""";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.jsonPath("$.grid.columns").value(2))
		.andExpect(MockMvcResultMatchers.jsonPath("$.grid.rows").value(2))
		.andExpect(MockMvcResultMatchers.jsonPath("$.sorted_images").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.sorted_images[0].id").value("408468fd-ce11"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.sorted_images[1].id").value("408468fd-ce11"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.sorted_images[2].id").value("408468fd-ce11"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.sorted_images[3].id").value("408468fd-ce11"));		
				
	}


	@Test
	public void missingParameterEmtpyObjTest() throws Exception {
		// when we got an emtpy object as request body we expect it reports the missing grid field
		String testJson = "{}";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("\"grid\" must not be null")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}

	@Test
	public void missingParameterMissingGrid() throws Exception {
		// when we got an emtpy object as request body we expect it reports the missing grid field
		String testJson = """
							{
				  "images": [
				    {
				      "id": "408468fd-ce11",
				      "embedding": "ZGM2NmFhMDgtZmFmYS00YzZmLWFjNWYtYzRmMmI5N2Q2OTE1Cg==",
				      "size_cols": 2,
				      "size_rows": 2,
				      "seed_column": 0,
				      "seed_row": 0
				    }
				  ]
				}
							""";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("\"grid\" must not be null")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}

	@Test
	public void missingImageIdTest() throws Exception {
		String testJson = """
							{
				  "images": [
				    {
				      "embedding": "ZGM2NmFhMDgtZmFmYS00YzZmLWFjNWYtYzRmMmI5N2Q2OTE1Cg==",
				      "size_cols": 2,
				      "size_rows": 2,
				      "seed_column": 0,
				      "seed_row": 0
				    }
				  ],
				  "grid": {
				    "aspect_ratio": 1.0,
				    "wrapped_mode": true,
				    "size_factor": 0
				  }
				}
							""";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("images[0].id")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("must not be null")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}

	@Test
	public void missingImageEmbeddingTest() throws Exception {
		String testJson = """
							{
				  "images": [
				    {
				      "id": "408468fd-ce11", 
				      "size_cols": 2,
				      "size_rows": 2,
				      "seed_column": 0,
				      "seed_row": 0
				    }
				  ],
				  "grid": {
				    "aspect_ratio": 1.0,
				    "wrapped_mode": true,
				    "size_factor": 0
				  }
				}
							""";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("images[0].embedding")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("must not be null")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}

	@Test
	public void invalidImageIdTest() throws Exception {
		String testJson = """
							{
				  "images": [
				    {
				      "id": ,
				      "embedding": "ZGM2NmFhMDgtZmFmYS00YzZmLWFjNWYtYzRmMmI5N2Q2OTE1Cg==",
				      "size_cols": 2,
				      "size_rows": 2,
				      "seed_column": 0,
				      "seed_row": 0
				    }
				  ],
				  "grid": {
				    "aspect_ratio": 1.0,
				    "wrapped_mode": true,
				    "size_factor": 0
				  }
				}
							""";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("images")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("expected a value")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}

	@Test
	public void invalidImageIdEmtpyTest() throws Exception {
		String testJson = """
							{
				  "images": [
				    {
				      "id": "",
				      "embedding": "ZGM2NmFhMDgtZmFmYS00YzZmLWFjNWYtYzRmMmI5N2Q2OTE1Cg==", 
				      "size_cols": 2,
				      "size_rows": 2,
				      "seed_column": 0,
				      "seed_row": 0
				    }
				  ],
				  "grid": {
				    "aspect_ratio": 1.0,
				    "wrapped_mode": true,
				    "size_factor": 0
				  }
				}
							""";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("images[0].id")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("size must be between 1")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}

	@Test
	public void invalidImageEmbeddingEmtpyTest() throws Exception {
		String testJson = """
							{
				  "images": [
				    {
				      "id": "ZGM2NmFhMDgtZmF",
				      "embedding": ""
				    }
				  ],
				  "grid": {
				    "aspect_ratio": 1.0,
				    "wrapped_mode": true,
				    "size_factor": 0
				  }
				}
							""";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("images[0].embedding")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("size must be between 1")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}

	@Test
	public void missingImagesTest() throws Exception {
		String testJson = """
							{
				  "grid": {
				    "aspect_ratio": 1.0,
				    "wrapped_mode": true,
				    "size_factor": 0
				  }
				}
							""";
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("images")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("must not be null")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}
	
	@Test
	public void conflictingSeedsMultipleTest() throws Exception {
		int numElements = 20;
		int[][] seeds = new int[][] {
			null,
			{1, 2}, // 1: A
			null,
			{1, 2}, // 3: A
			null,
			{3, 3},
			null,
			{2, 1}, // 7: B
			null,
			{2, 1}, // 9: B
			null,
			{2, 2}, // 11: C
			{2, 2}, // 12: C
			null,
			null,
			{3, 1},
			null,
			{1, 3},
			null,
			{2, 3}
		};
		int[][] sizes = null;
		byte[] testJson = getRequestBody(dataCsv, 10, 10, false, numElements, seeds, sizes);		
		System.out.println(new String(testJson));
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("Found 3 collisions")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("id=1 and id=3 collide on col=1 row=2")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("id=11 and id=12 collide on col=2 row=2")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("id=7 and id=9 collide on col=2 row=1")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}
	
	@Test
	public void conflictingSeedTest() throws Exception {
		int numElements = 10;
		int[][] seeds = new int[][] {
			{1, 2},
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			{1, 2}
		};
		int[][] sizes = null;
		byte[] testJson = getRequestBody(dataCsv, 10, 10, false, numElements, seeds, sizes);		
		System.out.println(new String(testJson));
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("Found 1 collision")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("id=0 and id=9")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}
	
	@Test
	public void elementSeedOutOfBounce() throws Exception {
		String testJson = """
						  {
							  "images": [
							    {
							      "id": "foo",
							      "embedding": "ZGM2NmFhMDgtZmFmYS00YzZmLWFjNWYtYzRmMmI5N2Q2OTE1Cg==",
							      "seed_column": 8,
							      "seed_row": 3,
							      "size_cols": 2,
							      "size_rows": 2
							    }
							  ],
							  "grid": {
							    "wrapped_mode": true,
							    "seed": 42,
							    "aspect_ratio": 1.33,
							    "size_factor": 17
							  }
						   }
						   """;
		System.out.println(testJson);
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("element foo")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("seed_column, seed_row is (8, 3)")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("grid dims are (3, 2)")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("Please adjust its seed position to (2, 1)")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}
	
	@Test
	public void oversizedOverflowingConflictTest() throws Exception {
		int numElements = 10;
		int[][] seeds = new int[][] {
			{3, 4},
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null
		};
		int[][] sizes = new int[][] {
			{2, 2},
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null
		};
		byte[] testJson = getRequestBody(dataCsv, 10, 10, false, numElements, seeds, sizes);		
		System.out.println(new String(testJson));
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is4xxClientError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("Overflowing grid")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("seed_column, seed_row is (3, 4)")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("size_cols, size_rows is (2, 2)")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("grid dims are (4, 4)")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.error").value(Matchers.containsString("Please adjust its seed position to (3, 3)")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").value(Matchers.startsWith(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))
		.andExpect(MockMvcResultMatchers.jsonPath("$.path").value(Matchers.startsWith(new String("/sort"))));
	}

	@Test
	public void realDataTest() throws Exception {
		// load data with 100 images
		byte[] testJson = getRequestBody(dataCsv, 10, 10, false, 100, null, null);
		System.out.println(testJson.toString());
		mvc.perform(MockMvcRequestBuilders
				.post("/sort/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(testJson)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
		.andExpect(MockMvcResultMatchers.jsonPath("$.grid.columns").value(11))
		.andExpect(MockMvcResultMatchers.jsonPath("$.grid.rows").value(11))
		.andExpect(MockMvcResultMatchers.jsonPath("$.sorted_images").isArray());
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
		// create image list
		JSONArray images = new JSONArray();
		CSVReaderDataset dataset = new CSVReaderDataset(csvFile);
		EmbeddingData[] csvItems = dataset.getAll();
		if (numToUse<=0) numToUse = csvItems.length;
		for (int a=0;a<numToUse;a+=1) {
			// create the image object
			JSONObject img = new JSONObject();
			//img.put("id", csvItems[a].getId());
			img.put("id", a);
			String base64Embedding = Base64.getEncoder().encodeToString(EmbeddingData.floatToByteArray(csvItems[a].getFeature()));
			img.put("embedding", base64Embedding);
			// seeds
			if (seeds != null && seeds[a] != null) {
				img.put("seed_column", seeds[a][0]);
				img.put("seed_row", seeds[a][1]);
			}
			// sizes
			if (sizes != null && sizes[a] != null) {
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
		System.out.println(requestBodyStr);
		// return bytes to include into POST request body
		return requestBodyStr.getBytes();
	}
	
}
