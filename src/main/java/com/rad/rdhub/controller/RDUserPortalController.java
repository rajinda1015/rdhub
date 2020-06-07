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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rad.rdhub.configs.RDCache;
import com.rad.rdhub.validator.RDBadRequestException;

@RestController
@RefreshScope
@RequestMapping("/userportal")
public class RDUserPortalController {

	private static final Logger LOGGER = LogManager.getLogger(RDUserPortalController.class);
	
	@Autowired
	private RDCache rdCache;

	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/findUsers", method = RequestMethod.GET)
	public ResponseEntity<?> fineUsers(
			@RequestParam Map<String, String> paramMap,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		LOGGER.info("Request : Find users by " + paramMap.get("username"));
		
		if (null == paramMap.get("username") || paramMap.get("username").isEmpty()) {
			throw new RDBadRequestException(HttpStatus.BAD_REQUEST + "[Invalid parameters. Please check the parameters before submit the request]");
		}
		
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
	}
	
	
}
