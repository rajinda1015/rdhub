package com.rad.rdhub.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rad.rdhub.configs.RDCache;
import com.rad.rdhub.dto.RDContactDTO;
import com.rad.rdhub.dto.RDLoginDTO;
import com.rad.rdhub.util.RDHubConstancts;
import com.rad.rdhub.validator.RDCommonValidator;

@RestController
@RefreshScope
@RequestMapping("/userportal/user")
public class RDUserPortalController {

	private static final Logger LOGGER = LogManager.getLogger(RDUserPortalController.class);
	
	@Autowired
	private RDCache rdCache;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RDCommonValidator validator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/addContactDetails", method = RequestMethod.POST)
	public ResponseEntity<?> addContactDetails(
			@RequestParam Map<String, String> paramMap,
			@RequestBody RDContactDTO[] contacts,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		LOGGER.info("Request : Create contacts by : " + paramMap.get("username"));
		
		List<String> messages = validator.validateContacts(contacts, RDHubConstancts.RECORD_ADD);
		if (null == messages) {
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/user/addContactDetails"
					+ "?username=" + paramMap.get("username");
	
			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(contacts, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, responseType);
			messages = resEntity.getBody();
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}

	@RequestMapping(value = "/updateContactDetails", method = RequestMethod.PUT)
	public ResponseEntity<?> updateContactDetails(
			@RequestParam Map<String, String> paramMap,
			@RequestBody RDContactDTO[] contacts,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		LOGGER.info("Request : Update contacts by : " + paramMap.get("username"));
		
		List<String> messages = validator.validateContacts(contacts, RDHubConstancts.RECORD_UPDATE);
		if (null == messages) {
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/user/updateContactDetails"
					+ "?username=" + paramMap.get("username");
	
			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(contacts, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.PUT, entity, responseType);
			messages = resEntity.getBody();
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}

	@RequestMapping(value = "/deleteContactDetails", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteContactDetails(
			@RequestParam Map<String, String> paramMap,
			@RequestBody Long[] dids,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		LOGGER.info("Request : Delete contacts by : " + paramMap.get("username"));
		
		List<String> messages = validator.validateContactDidsToDelete(dids);
		if (null == messages) {
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/user/deleteContactDetails"
					+ "?username=" + paramMap.get("username");
	
			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(dids, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.DELETE, entity, responseType);
			messages = resEntity.getBody();
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}
	
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(
			@RequestParam Map<String, String> paramMap,
			@RequestBody RDLoginDTO loginDTO,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		LOGGER.info("Request : Register user. User : " + loginDTO.getUsername());
		
		List<String> messages = validator.validateRegisterUser(loginDTO);
		if (null == messages) {
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/findUsers"
					+ "?username=" + paramMap.get("username");
	
			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, responseType);
			List<String> users = resEntity.getBody();
			
			return ResponseEntity.status(HttpStatus.OK).body("Testing method : " + users);

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}
	
	
}
