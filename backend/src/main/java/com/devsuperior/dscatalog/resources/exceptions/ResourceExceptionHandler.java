package com.devsuperior.dscatalog.resources.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	// SEMPRE QUE TIVER ALGUMA EXCEÇÃO DESSE TIPO, O CONTROLADOR VAI SER DIRECIONADO PRA CÁ
	@ExceptionHandler(ResourceNotFoundException.class) 
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException ex, HttpServletRequest request)
	{
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Resource not found");
		err.setMessage(ex.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	// DATABASE EXCEPTION
	@ExceptionHandler(DataBaseException.class) 
	public ResponseEntity<StandardError> database(DataBaseException ex, HttpServletRequest request)
	{
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value()); // 400
		err.setError("Database exception");
		err.setMessage(ex.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	// MethodArgumentNotValidException
	@ExceptionHandler(MethodArgumentNotValidException.class) 
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException ex, HttpServletRequest request)
	{
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY; // 422 -> ALGUMA ENTIDADE NÃO FOI POSSÍVEL DE SER PROCESSADA
		ValidationError err = new ValidationError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value()); // 400
		err.setError("Validation exception");
		err.setMessage(ex.getMessage());
		err.setPath(request.getRequestURI());
		
		
		for (FieldError f : ex.getBindingResult().getFieldErrors()) {
			err.addError(f.getField(), f.getDefaultMessage());
		}
		

		return ResponseEntity.status(status).body(err);
	}
}
