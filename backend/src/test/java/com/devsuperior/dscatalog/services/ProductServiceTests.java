package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	
	private PageImpl<Product> page;
	private Product product;
	
	private Category category;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		category = Factory.createCategory();
		
		//CONFIGURAR O COMPORTAMENTO SIMULADO
		
		//VOID (DELETE)
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
		// METODO QUE RETORNA ALGO
		
		//FIND ALL
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		//SAVE DO REPOSITORY
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		//FINDBYID COM ID EXISTENTE E N EXISTENTE
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		//UPDATE -> GETONE
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.when(repository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		//NÃO CHAMAR EXCEÇÃO
		Assertions.assertDoesNotThrow( () -> {
			service.delete(existingId);
		} );
		
		//VERIFICAR SE O MÉTODO FOI CHAMADO NO TESTE
		Mockito.verify(repository).deleteById(existingId);
		//Mockito.verify(repository, Mockito.never()).deleteById(existingId);
		//Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		} );
		
		Mockito.verify(repository).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteShouldThrowDataBaseExceptionWhenIdIsDependent() {
		Assertions.assertThrows(DataBaseException.class, () -> {
			service.delete(dependentId);
		} );
		
		Mockito.verify(repository).deleteById(dependentId);
	}
	
	@Test
	public void findAllShouldReturnPage() {
		Pageable pageable = PageRequest.of(0,10);
		
		Page<ProductDTO> result = service.findAllPaged(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repository).findAll(pageable);
	}
	
	// FIND BY ID DEVERIA:
	// RETORNAR UM PRODUCTDTO QUANDO O ID EXISTIR, RETORNAR UM RESOURCENOTFOUNDEXCEPTION QUANDO O ID NÃO EXISTIR
	
	@Test
	public void findByIdShouldReturnObjectWhenExistingId() {
		
		ProductDTO result = service.findById(existingId);
		
		Assertions.assertNotNull(result);
		
		Mockito.verify(repository).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenNonExistingId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		} );
		
		Mockito.verify(repository).findById(nonExistingId);
		
	}
	
	// UPDATE DEVERIA:
	//RETORNAR UM PRODUCTDTO QUANDO O ID EXISTIR, LANÇAR UMA RESOURCENOTFOUNDEXCEPTION QUANDO O ID N EXISTIR
	@Test
	public void updateShouldReturnProductDtoWhenExistingId() {
		
		ProductDTO productDTO = Factory.createProductDTO();
		
		ProductDTO result = service.update(existingId, productDTO);
		
		Assertions.assertNotNull(result);
		
		//Mockito.verify(service).update(existingId,result);
	}
	
	@Test
	public void updateShouldThrowEntityNotFoundExceptionWhenNonExistingId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			ProductDTO productDto = Factory.createProductDTO();
			service.update(nonExistingId,productDto);
		} );
		
		//Mockito.verify(repository).findById(nonExistingId);
	}
}
