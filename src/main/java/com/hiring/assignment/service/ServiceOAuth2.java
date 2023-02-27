package com.hiring.assignment.service;

import com.hiring.assignment.oauth2.service.UserEntity;
import com.hiring.assignment.singleton.SingletonLoggedInUsers;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;

@Service
public class ServiceOAuth2 {
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	JwtTokenStore tokenStore;

	@Autowired
	DefaultTokenServices defaultTokenServices;

	public String getJwtToken(UserEntity dtoUserEntity) throws URISyntaxException, ParseException {
		// now get a new token
		final String baseUrl = "http://localhost:8080/oauth/token";
		URI uri = new URI(baseUrl);

		// preparing headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic Y2xpZW50OmNsaWVudA==");

		// preparing body
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("username", dtoUserEntity.getUsername());
		map.add("password", dtoUserEntity.getUsername());
		map.add("grant_type", "password");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
		// do post call
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		if (response.getStatusCode().name().contentEquals(HttpStatus.OK.name())) {
			JSONParser parser = new JSONParser();
			JSONObject parse = (JSONObject) parser.parse(response.getBody());
			String newAccessToken = parse.get("access_token").toString();
			UserEntity userEntity = SingletonLoggedInUsers.getInstance().getUserCredentials()
					.get(dtoUserEntity.getUsername());
			if (userEntity == null) {
				userEntity = new UserEntity();
				userEntity.setUsername(dtoUserEntity.getUsername());
			}
			userEntity.setAccessToken(newAccessToken);
			SingletonLoggedInUsers.getInstance().getUserCredentials().put(dtoUserEntity.getUsername(), userEntity);
			return newAccessToken;
		}
		return null;
	}

	public Boolean revokAllTokens() {
		SingletonLoggedInUsers.getInstance().setUserCredentials(new LinkedHashMap<String, UserEntity>());
		return true;
	}

}
