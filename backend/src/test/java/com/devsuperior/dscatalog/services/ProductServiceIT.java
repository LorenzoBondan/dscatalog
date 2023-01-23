package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional // CADA TESTE É INDEPENDENTE UM DO OUTRO. EX: NO TESTE DELETE, FICA COM UM ITEM A MENOS. ENTÃO O COUNTTOTALPRODUCTS DO TESTE SEGUINTE DIMINUI
public class ProductServiceIT {

	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	// DELETE
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		
		service.delete(existingId);
		
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
		
	}
	
	@Test
	public void deleteShouldResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		} );
		
	}
	
	@Test
	public void findAllPagedShouldReturnPageWhenPage0Size10() {
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAllPaged(0L, "", pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber()); // NUMERO DA PAGINA
		Assertions.assertEquals(10, result.getSize()); // TAMANHO DA PAGINA
		Assertions.assertEquals(countTotalProducts, result.getTotalElements()); // TOTAL DE ELEMENTOS
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		
		PageRequest pageRequest = PageRequest.of(50, 10); //SÓ 25 ELEMENTOS, NÃO TERIA UMA PAGINA 50
		
		Page<ProductDTO> result = service.findAllPaged(0L, "", pageRequest);
		
		Assertions.assertTrue(result.isEmpty());

	}
	
	// TESTAR SE OS DADOS ESTÃO ORDENADOS QUANDO SE MANDA BUSCAR ORDENADAMENTE
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name")); 
		
		Page<ProductDTO> result = service.findAllPaged(0L, "", pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		
		//OLHAR OS ELEMENTOS DO BANCO E VER QUAIS SERIAM OS 3 PRIMEIROS EM ORDEM ALFABÉTICA
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
	}
}
