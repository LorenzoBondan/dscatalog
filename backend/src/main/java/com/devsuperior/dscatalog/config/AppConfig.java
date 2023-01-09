package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {

	@Bean // ANOTATION DE MÉTODO
	public BCryptPasswordEncoder passwordEncoder() { // DÁ PRA INJETAR ESSE CARA EM OUTRAS CLASSES
		return new BCryptPasswordEncoder();
	}
}
