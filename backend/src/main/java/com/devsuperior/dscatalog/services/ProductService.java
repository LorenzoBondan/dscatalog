package com.devsuperior.dscatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	// TRAZER TUDO
	
	//@Transactional(readOnly = true)
	//public List<ProductDTO> findAll()
	//{
	//	List<Product> list = repository.findAll();
	//	return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
	//}
	
	// TRAZER TUDO PAGINADO
	//@Transactional(readOnly = true)
	//public Page<ProductDTO> findAllPaged(Pageable pageable)
	//{
		//Page<Product> list = repository.findAll(pageable);
		//return list.map(x -> new ProductDTO(x));
	//}
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, Pageable pageable) {
	 	List<Category> categories = (categoryId == 0) ? null :
	 		Arrays.asList(categoryRepository.getOne(categoryId));
	 	Page<Product> page = repository.find(categories, name, pageable);
	 	repository.findProductsWithCategories(page.getContent()); // GETCONTENT JÁ CONVERTE A PAGINA PRA LISTA
	 	return page.map(x -> new ProductDTO(x, x.getCategories()));
	}

	

	//METODO DE BUSCAR PRODUTO POR ID
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) 
	{
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new ProductDTO(entity, entity.getCategories());

	}
	


	@Transactional
	public ProductDTO insert(ProductDTO dto)
	{
		Product entity = new Product();
		//entity.setName(dto.getName());
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			//entity.setName(dto.getName());
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		// CASO VC TENTE DELETAR UMA CATEGORIA COM PRODUTOS VINCULADOS
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation");
		}
	}
	
	// MÉTODO AUXILIAR PRA COPIAR TODAS AS PROPRIEDADES DA DTO PRA ENTITY
	// USADO NO INSERT E NO UPDATE PRA TODOS OS ATRIBUTOS
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		// COPIAR AS CATEGORIAS DO DTO PARA A ENTIDADE
		entity.getCategories().clear();
		
		for(CategoryDTO catDto : dto.getCategories()) { //FOREACH
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}
}
