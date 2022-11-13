package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll()
	{
		List<CategoryDTO> list = service.findAll();	
		return ResponseEntity.ok().body(list);
	}
	
	//TRAZENDO CATEGORIA POR ID
	
	@GetMapping(value = "/{id}") //ARGUMENTO ACRESCENTADO NA FRENTE DA ROTA BÁSICA
	// /CATEGORIES/ID
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) //PARÂMETRO TEM QUE FICAR IGUAL AO CAMINHO ACIMA | PathVariable serve para casar a variável do método com esta
	{
		CategoryDTO dto = service.findById(id);	
		return ResponseEntity.ok().body(dto);
	}
}
