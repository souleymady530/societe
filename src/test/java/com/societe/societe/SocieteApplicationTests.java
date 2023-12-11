package com.societe.societe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.societe.societe.Services.ImportFileService;

@SpringBootTest
@AutoConfigureMockMvc
class SocieteApplicationTests {

	  @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImportFileService employeeService;


	@Test
	void contextLoads() {
	}

	@Test 
	public void testImportTxt() throws Exception{
		MockMultipartFile mmf=new MockMultipartFile("file", "uploads/texte.txt","text-plain","Green Learner - Arvind".getBytes());
		this.mockMvc.perform(multipart("/import").file(mmf)).andExpect(status().isOk()); 
		 
	}

	@Test 
	public void testImportJSON() throws Exception{
		 	MockMultipartFile mmf=new MockMultipartFile("file", "uploads/clients.json","text-plain","Green Learner - Arvind".getBytes());
		this.mockMvc.perform(multipart("/import").file(mmf)).andExpect(status().isOk()); 
	}
	@Test 
	public void testImportCSV() throws Exception{
		 	MockMultipartFile mmf=new MockMultipartFile("file", "uploads/clients.csv","text-plain","Green Learner - Arvind".getBytes());
		this.mockMvc.perform(multipart("/import").file(mmf)).andExpect(status().isOk()); 
	}
	@Test 
	public void testImportXML() throws Exception{
		 	MockMultipartFile mmf=new MockMultipartFile("file", "uploads/clients.xml","text-plain","Green Learner - Arvind".getBytes());
		this.mockMvc.perform(multipart("/import").file(mmf)).andExpect(status().isOk()); 
	}
	 

	@Test
	public void TestgetStatistique() throws Exception{
			mockMvc.perform(MockMvcRequestBuilders.get("/statistique").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}


}
