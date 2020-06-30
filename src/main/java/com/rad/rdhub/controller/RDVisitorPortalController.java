package com.rad.rdhub.controller;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rad.rdhub.configs.RDCache;
import com.rad.rdhub.dto.RDLoginDTO;
import com.rad.rdhub.util.RDHubConstancts;
import com.rad.rdhub.validator.RDCommonValidator;

@RestController
@RefreshScope
@RequestMapping(value = "/userportal/visitor")
public class RDVisitorPortalController {

	private static final Logger LOGGER = LogManager.getLogger(RDVisitorPortalController.class);

	@Autowired
	private RDCache rdCache;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RDCommonValidator validator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody RDLoginDTO loginDTO) throws Exception {
		LOGGER.info("Request : Register user : " + loginDTO.getUsername());
		
		List<String> messages = validator.validateRegisterUser(loginDTO);
		if (null == messages) {
			loginDTO.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
			loginDTO.setConfimPwd(passwordEncoder.encode(loginDTO.getConfimPwd()));
			loginDTO.setStatus(RDHubConstancts.ACTIVE);
			
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/visitor/registerUser";
	
			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			HttpEntity<Object> entity = new HttpEntity<Object>(loginDTO, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, responseType);
			messages = resEntity.getBody();
			
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}
}
