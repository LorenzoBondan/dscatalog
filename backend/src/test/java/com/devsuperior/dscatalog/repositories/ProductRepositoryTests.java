package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		//CHAMAR PRA VER SE DELETOU MESMO E TESTAR
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDontExists() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repository.deleteById(nonExistingId);
		} );
	}
	
	// TESTANDO SAVE COM ID NULO - INSERT
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	/* findById DEVERIA:
	 RETORNAR UM OPTIONAL<PRODUCT> NÃO VAZIO QUANDO O ID EXISTIR
	 RETORNAR UM OPTIONAL<PRODUCT> VAZIO QUANDO O ID NÃO EXISTIR
	 */
	
	@Test
	public void findByIdShouldReturnNotEmptyObjectWhenIdExists() {
		
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnEmptyObjectWhenIdNotExists() {
		
		Optional<Product> result = repository.findById(nonExistingId);
		
		Assertions.assertFalse(result.isPresent());
	}
}
