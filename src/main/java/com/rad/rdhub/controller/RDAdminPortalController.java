package com.rad.rdhub.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rad.rdhub.configs.RDCache;
import com.rad.rdhub.dto.RDUserDTO;
import com.rad.rdhub.util.RDHubConstancts;

@RestController
@RefreshScope
@RequestMapping("/userportal/admin")
public class RDAdminPortalController {

	private static final Logger LOGGER = LogManager.getLogger(RDAdminPortalController.class);
	
	@Autowired
	private RDCache rdCache;

	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ResponseEntity<?> addUser(
			@RequestParam Map<String, String> paramMap, 
			@RequestBody RDUserDTO user,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		
		LOGGER.info("Request : User instance save by " + paramMap.get("username"));
		
		List<String> messages = validate(user, RDHubConstancts.USER_ADD);
		if (messages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[" + messages.toString() + "]");
		} else {
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/admin/addUser"
					+ "?username=" + paramMap.get("username");

			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(user, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, responseType);
			messages = resEntity.getBody();
			return ResponseEntity.status(HttpStatus.OK).body("[" + messages.toString() + "]");
		}
	}
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(
			@RequestParam Map<String, String> paramMap,
			@RequestBody RDUserDTO user,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		
		LOGGER.info("Request : User instance updated by " + paramMap.get("username"));
		
		List<String> messages = validate(user, RDHubConstancts.USER_UPDATE);
		if (messages.size() > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[" + messages.toString() + "]");
		} else {
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/admin/updateUser"
					+ "?username=" + paramMap.get("username");

			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(user, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.PUT, entity, responseType);
			messages = resEntity.getBody();
			return ResponseEntity.status(HttpStatus.OK).body("[" + messages.toString() + "]");
		}
	}
	
	private List<String> validate(RDUserDTO user, int action) throws Exception {
		List<String> messageList = new ArrayList<String>();

		if (action == RDHubConstancts.USER_ADD && user.getUserDid() > 0) {
			messageList.add("Cannot insert existing user again");
		}

		if (action == RDHubConstancts.USER_UPDATE && user.getUserDid() == 0) {
			messageList.add("Please add user details before update the details");
		}

		if (null == user.getFirstName() || user.getFirstName().isEmpty()) {
			messageList.add("First Name cannot be an empty field");
		}
		
		if (null == user.getMiddleName() || user.getMiddleName().isEmpty()) {
			messageList.add("Middle Name cannot be an empty field");
		}
		
		if (null == user.getLastName() || user.getLastName().isEmpty()) {
			messageList.add("Last Name cannot be an empty field");
		}
		
		Character defaultVal = new Character(Character.MIN_VALUE);
		if (0 == defaultVal.compareTo((Character) user.getGender())) {
			messageList.add("Gender cannot be an empty field");
		}
		
		return messageList;
	}
}
