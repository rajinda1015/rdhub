package com.rad.rdhub.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rad.rdhub.configs.RDCache;
import com.rad.rdhub.dto.RDLoginDTO;
import com.rad.rdhub.validator.RDBadRequestException;

@RestController
@RefreshScope
@RequestMapping("/rad")
public class RDHubController {

	private static final Logger LOGGER = LogManager.getLogger(RDHubController.class);
	
	@Value("${application.client.id}")
	private String root_client_id;

	@Value("${application.client.secret}")
	private String root_client_secret;
	
	@Value("${security.oauth.server.url.token.mapping.endpoint}")
	private String authUrlEndPoint;
	
	@Value("${security.oauth.server.url}")
	private String authServerUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RDCache rdCache;

	@RequestMapping(value = "/getToken", method = RequestMethod.POST)
	public ResponseEntity<?> getTokenForUser(@RequestParam Map<String, String> paramMap) throws Exception {
		LOGGER.info("Request: Token request by : " + paramMap.get("username"));
		
		if ( (null == paramMap.get("username") || paramMap.get("username").isEmpty())
			 || (null == paramMap.get("password") || paramMap.get("password").isEmpty())) {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Invalid parameters. Please check the parameters before submit the request]");
		}
		
		String authorization = new String(Base64.encodeBase64((root_client_id + ":" + root_client_secret).getBytes()));
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + authorization);
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(authUrlEndPoint)
				.append("?username=").append(paramMap.get("username"))
				.append("&password=").append(paramMap.get("password"))
				.append("&grant_type=").append("password");
		
		ResponseEntity<String> response = restTemplate.exchange(sbuffer.toString(), HttpMethod.POST, httpEntity, String.class);
		if (200 == response.getStatusCodeValue()) {
			// Update login time
			RDLoginDTO login = new RDLoginDTO();
			login.setUsername(paramMap.get("username"));
			
			JsonParser parser = new JsonParser();
			JsonElement rootNode = parser.parse(response.getBody());
			JsonObject rootObject = rootNode.getAsJsonObject();
			
			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + rootObject.get("access_token").getAsString());
			HttpEntity<Object> httpLoginEntity = new HttpEntity<Object>(login, httpHeader);
			
			sbuffer.setLength(0);
			sbuffer.append(rdCache.getUserPortalInfo());
			sbuffer.append("/userportal/user/updateLastLogin?username=" + paramMap.get("username"));
			ResponseEntity<String> loginResponse = restTemplate.exchange(sbuffer.toString(), HttpMethod.PUT, httpLoginEntity, String.class);
			LOGGER.info("Last login update : " + loginResponse.getBody());
			
			return ResponseEntity.status(HttpStatus.OK).body(response.getBody());

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
		}
	}
	
	@RequestMapping(value = "/getTokenByRefreshToken", method = RequestMethod.GET)
	public ResponseEntity<?> getTokenByRefreshToken(
			@RequestParam Map<String, String> paramMap, @RequestHeader(name = "rfToken", required = true) String rfToken) 
		throws Exception {
		LOGGER.info("Request: Refresh token requested by : " + paramMap.get("username"));
		
		if ( null == paramMap.get("username") || paramMap.get("username").isEmpty() || rfToken.isEmpty() ) {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Invalid parameters. Please check the parameters before submit the request]");
		}

		String authorization = new String(Base64.encodeBase64((root_client_id + ":" + root_client_secret).getBytes()));
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization",  "Basic " + authorization);
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(authUrlEndPoint)
				.append("?grant_type=").append("refresh_token")
				.append("&refresh_token=").append(rfToken);
		
		ResponseEntity<String> response = restTemplate.exchange(sbuffer.toString(), HttpMethod.POST, httpEntity, String.class);
		if (200 == response.getStatusCodeValue()) {
			return ResponseEntity.status(HttpStatus.OK).body(response.getBody());

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid token parameter");
		}
	}
	
	@RequestMapping(value = "/revokeToken", method = RequestMethod.GET)
	public ResponseEntity<?> revokeToken(@RequestParam Map<String, String> paramMap) throws Exception {
		
		LOGGER.info("Request: Logged out user : " + paramMap.get("username"));
		
		if ( null == paramMap.get("username") || paramMap.get("username").isEmpty() ) {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Invalid arguments]");
		}
		
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(authServerUrl)
			.append("tokenManager/revokeAccessToken")
			.append("?username=").append(paramMap.get("username"))
			.append("&client=").append(new String(Base64.encodeBase64(root_client_id.getBytes())));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(sbuffer.toString(), HttpMethod.GET, httpEntity, String.class);
		return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
	}

	@RequestMapping(value = "/removeRFToken", method = RequestMethod.GET)
	public ResponseEntity<?> removeRefreshToken(
			@RequestParam Map<String, String> paramMap,
			@RequestHeader(name = "rfToken", required = true) String rfToken) throws Exception {
		
		LOGGER.info("Request: refresh token owned by : " + paramMap.get("username"));
		
		if ( null == paramMap.get("username") || paramMap.get("username").isEmpty() ) {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST.value() + " : [Invalid arguments]");
		}
		
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(authServerUrl)
			.append("tokenManager/removeRFToken")
			.append("?username=").append(paramMap.get("username"));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("rfToken", rfToken);
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(sbuffer.toString(), HttpMethod.GET, httpEntity, String.class);
		return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
	}
	
}
