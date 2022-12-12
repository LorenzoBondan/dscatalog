package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@Autowired
	private CategoryService service;
	
	// TRAZENDO TUDO
	
	//@GetMapping
	//public ResponseEntity<List<CategoryDTO>> findAll()
	//{
	//	List<CategoryDTO> list = service.findAll();	
	//	return ResponseEntity.ok().body(list);
	//}
	
	// TRAZENDO TUDO DE FORMA PAGINADA
	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable)
	{		
		Page<CategoryDTO> list = service.findAllPaged(pageable);	
		return ResponseEntity.ok().body(list);
	}
	
	//TRAZENDO CATEGORIA POR ID
	
	@GetMapping(value = "/{id}") //ARGUMENTO ACRESCENTADO NA FRENTE DA ROTA BÁSICA
	// /CATEGORIES/ID
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) //PARÂMETRO TEM QUE FICAR IGUAL AO CAMINHO ACIMA | PathVariable DADO OBRIGATÓRIO
	{
		CategoryDTO dto = service.findById(id);	
		return ResponseEntity.ok().body(dto);
	}
	
	// INSERINDO NOVA CATEGORIA
	@PostMapping
	public ResponseEntity<CategoryDTO> insert (@RequestBody CategoryDTO dto)
	{
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);	
	}
	
	// ATUALIZANDO CATEGORIA -> É UMA MISTURA DO BUSCA POR ID COM POST
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto)
	{
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	// DELETANDO CATEGORIA
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> delete(@PathVariable Long id)
	{
		service.delete(id);
		return ResponseEntity.noContent().build(); // RESPOSTA 204 = DEU CERTO, COM O CORPO VAZIO
	}
	
}
