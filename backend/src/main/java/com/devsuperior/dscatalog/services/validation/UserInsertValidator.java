package com.devsuperior.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		
		// IFs ADICIONANDO POSSÍVEIS ERROS À LISTA
		// if() {
		// 		list.add(e);
		// }
		
		// TESTE PRA VER SE O EMAIL QUE VEM DO DTO JÁ EXISTE NO BANCO
		
		// TIVEMOS QUE FAZER O METODO QUE BUSQUE O EMAIL POR ID -> FAZER NO USERREPOSITORY
		
		User user = repository.findByEmail(dto.getEmail());
		
		if (user != null) {
			// ENCONTROU UM EMAIL JÁ CADASTRADO
			list.add(new FieldMessage("email", "Este email já existe."));
		}
		
		// PERCORRE A LISTA PRA INSERIR OS ERROS
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty(); // SE A LISTA, QUE COMEÇOU VAZIA, RETORNAR VAZIA, QUER DIZER QUE NENHUM TESTE ENTROU
	}
}
