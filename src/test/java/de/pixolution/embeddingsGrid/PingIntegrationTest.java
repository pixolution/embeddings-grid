package de.pixolution.embeddingsGrid;

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

import de.pixolution.embeddingsGrid.invoker.OpenApiGeneratorApplication;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, 
                classes = OpenApiGeneratorApplication.class)
@AutoConfigureMockMvc
public class PingIntegrationTest {
	
    @Autowired
    private MockMvc mvc;
	
    @Test
    public void pingTest() throws Exception {
    	mvc.perform(MockMvcRequestBuilders
	    			.get("/ping/")
	    			.accept(MediaType.APPLICATION_JSON))
    	    .andDo(MockMvcResultHandlers.print())
	        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
	        .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Ping back"));
    }

}
