package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.Role;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService {

	//MACETE PRA LANÇAR MENSAGEM NO CONSOLE
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class); // É TIPO UMA MESSAGEBOX
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; // PRA ESCONDER A SENHA, CLASSE APPCONFIG
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	// TRAZER TUDO
	
	//@Transactional(readOnly = true)
	//public List<UserDTO> findAll()
	//{
	//	List<User> list = repository.findAll();
	//	return list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
	//}
	
	// TRAZER TUDO PAGINADO
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable)
	{
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}
	

	//METODO DE BUSCAR USUARIO POR ID
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) 
	{
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new UserDTO(entity); // AQUI MUDAMOS, OLHAR O CONSTRUTOR DO DTO
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto)
	{
		User entity = new User();
		copyDtoToEntity(dto, entity);
		
		entity.setPassword(passwordEncoder.encode(dto.getPassword())); // ESCONDER A SENHA
		
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
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
		// CASO VC TENTE DELETAR UM PERFIL COM USUÁRIOS VINCULADOS
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation");
		}
	}
	
	private void copyDtoToEntity(UserDTO dto, User entity) {
		
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

		
		// COPIAR AS CATEGORIAS DO DTO PARA A ENTIDADE
		entity.getRoles().clear();
		
		for(RoleDTO rolDto : dto.getRoles()) { //FOREACH
			Role role = roleRepository.getOne(rolDto.getId());
			entity.getRoles().add(role);
		}
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// METODO DO USERREPOSITORY QUE CRIAMOS, FINDBYEMAIL
		User user = repository.findByEmail(username);
		
		// SE NÃO EXISTIR, LANÇAR UMA UsernameNotFoundException
		if(user == null) {
			// MACETE PRA LANÇAR NO CONSOLE UMA MENSAGEM (LOGGER FOI INSTANCIADO ACIMA NA CLASSE)
			logger.error("User not found: " + username);
			throw new UsernameNotFoundException("Email not found");
		}
		
		logger.info("User found: " + username);
		return user;
	}
}
