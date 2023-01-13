package com.devsuperior.dscatalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class AppConfig {

	@Bean // ANOTATION DE MÉTODO
	public BCryptPasswordEncoder passwordEncoder() { // DÁ PRA INJETAR ESSE CARA EM OUTRAS CLASSES
		return new BCryptPasswordEncoder();
	}
	
	// COLADO DO MATERIAL DE APOIO "BEANS PARA TOKEN JWT"
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey("MY-JWT-SECRET");
		return tokenConverter;
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
}
