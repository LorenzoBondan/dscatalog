package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String firstName;
	private String lastName;
	private String email;

	
	
	private List<RoleDTO> roles = new ArrayList<>();
	  
	public UserDTO() {}

	
	public UserDTO(Long id, String firstName, String lastName, String email, String password) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;

	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	// SOMENTE O GET NAS LISTAS
	public List<RoleDTO> getRoles() { 
		return roles;
	}



	// construtor implantado na classe UserService
	public UserDTO(User entity) {
		this.id = entity.getId();
		this.firstName = entity.getFirstName();
		this.lastName = entity.getLastName();
		this.email = entity.getEmail();

		entity.getRoles().forEach(rol -> this.roles.add(new RoleDTO(rol)));
	}


	public UserDTO(User entity, Set<Role> roles) {
		this(entity); // EXECUTA TUDO DO METODO DE CIMA
		roles.forEach(rol -> this.roles.add(new RoleDTO(rol))); // PEGA CADA ELEMENTO E INSERE ELE TRANSFORMADO PARA DTO NA LISTA DE CATEGORIES DA CLASSE
	}
}
