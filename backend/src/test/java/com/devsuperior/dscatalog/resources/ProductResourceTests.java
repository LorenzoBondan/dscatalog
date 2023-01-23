package com.devsuperior.dscatalog.resources;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	
	private String username;
	private String password;
	
	@Autowired
	private ObjectMapper objectMapper;
		
	@BeforeEach
	void setUp() throws Exception{
		
		existingId = 1L; // MOCADO ENTÃO NÃO IMPORTA O VALOR
		nonExistingId = 2L;
		dependentId = 3L;
		
		username = "maria@gmail.com" ;
		password = "123456" ;
		
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
		Mockito.when(service.findAllPaged(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(page);
		
		// SIMULANDO O COMPORTAMENTO DO FINDBYID DO SERVICE
		Mockito.when(service.findById(existingId)).thenReturn(productDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		// SIMULANDO O COMPORTAMENTO DO UPDATE DO SERVICE
		Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
		Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
	
		// SIMULANDO O COMPORTAMENTO DO DELETE NO SERVICE (VOID)
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DataBaseException.class).when(service).delete(dependentId);
		
		// SIMULANDO O COMPORTAMENTO DO INSERT DO SERVICE
		Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);
		
	}
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		//CHAMA O PERFORM, CHAMA O MÉTODO HTTP COM O CAMINHO E EMENDA A ASSERTION
		mockMvc.perform(get("/products")).andExpect(status().isOk());
		
		/* FAZENDO SEPARADO
		 ResultAction result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		 result.andExpect(status().isOk());
		 */
	}
	
	// TESTES DO FINDBYID DO RESOURCE
	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		
		mockMvc.perform(get("/products/{id}", existingId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists()) //NO CORPO DA RESPOSTA, TEM QUE EXISTIR UM CAMPO CHAMADO ID
			.andExpect(jsonPath("$.name").exists())
			.andExpect(jsonPath("$.description").exists()); 
		
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		mockMvc.perform(get("/products/{id}", nonExistingId))
			.andExpect(status().isNotFound());
		
	}
	
	// TESTES DO update DO RESOURCE
	@Test
	public void updateShouldReturnProductWhenIdExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(put("/products/{id}",existingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists()) 
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.description").exists()); 
		
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(put("/products/{id}",nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		
	}
	
	/* DELETE DEVERIA:
	 * RETORNAR UM NO CONTENT (204) QUANDO O ID EXISTIR
	 * RETORNAR UM NOT FOUND (404) QUANDO O ID NÃO EXISTIR
	 */
	@Test
	public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(delete("/products/{id}",existingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(delete("/products/{id}",nonExistingId)
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		
	}
	
	// INSERT DEVERIA RETORNAR UM CREATED (201) BEM COMO UM PRODUCTDTO
	@Test
	public void insertShouldReturnProductDtoCreated() throws Exception{
		
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
		
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		mockMvc.perform(post("/products")
				.header("Authorization", "Bearer " + accessToken)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

	}
	
}
