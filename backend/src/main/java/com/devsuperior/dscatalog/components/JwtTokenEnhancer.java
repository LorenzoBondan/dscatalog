package com.devsuperior.dscatalog.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;

@Component
public class JwtTokenEnhancer implements TokenEnhancer {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		//TRAZER A INFORMAÇÃO A SER INCRESCENTADA NO TOKEN
		User user = userRepository.findByEmail(authentication.getName());
		
		// PARA ADICIONAR A INFORMAÇÃO AO TOKEN
		Map<String, Object> map = new HashMap<>();
		
		map.put("userFirstName", user.getFirstName());
		map.put("userId", user.getId());
		
		// DOWNCASTING
		DefaultOAuth2AccessToken token =  (DefaultOAuth2AccessToken) accessToken;
		token.setAdditionalInformation(map);
		
		return accessToken;
	}

}
