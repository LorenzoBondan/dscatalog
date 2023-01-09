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

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.services.RoleService;

@RestController
@RequestMapping(value = "/roles")
public class RoleResource {

	@Autowired
	private RoleService service;
	
	// TRAZENDO TUDO
	
	//@GetMapping
	//public ResponseEntity<List<RoleDTO>> findAll()
	//{
	//	List<RoleDTO> list = service.findAll();	
	//	return ResponseEntity.ok().body(list);
	//}
	
	// TRAZENDO TUDO DE FORMA PAGINADA
	@GetMapping
	public ResponseEntity<Page<RoleDTO>> findAll(Pageable pageable)
	{		
		Page<RoleDTO> list = service.findAllPaged(pageable);	
		return ResponseEntity.ok().body(list);
	}
	
	//TRAZENDO CATEGORIA POR ID
	
	@GetMapping(value = "/{id}") //ARGUMENTO ACRESCENTADO NA FRENTE DA ROTA BÁSICA
	// /ROLES/ID
	public ResponseEntity<RoleDTO> findById(@PathVariable Long id) //PARÂMETRO TEM QUE FICAR IGUAL AO CAMINHO ACIMA | PathVariable DADO OBRIGATÓRIO
	{
		RoleDTO dto = service.findById(id);	
		return ResponseEntity.ok().body(dto);
	}
	
	// INSERINDO NOVA CATEGORIA
	@PostMapping
	public ResponseEntity<RoleDTO> insert (@RequestBody RoleDTO dto)
	{
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);	
	}
	
	// ATUALIZANDO CATEGORIA -> É UMA MISTURA DO BUSCA POR ID COM POST
	@PutMapping(value = "/{id}")
	public ResponseEntity<RoleDTO> update(@PathVariable Long id, @RequestBody RoleDTO dto)
	{
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	// DELETANDO CATEGORIA
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<RoleDTO> delete(@PathVariable Long id)
	{
		service.delete(id);
		return ResponseEntity.noContent().build(); // RESPOSTA 204 = DEU CERTO, COM O CORPO VAZIO
	}
	
}
