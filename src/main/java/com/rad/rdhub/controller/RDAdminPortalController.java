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
import com.rad.rdhub.dto.RDLoginDTO;
import com.rad.rdhub.dto.RDUserDTO;
import com.rad.rdhub.dto.RDUserRoleDTO;
import com.rad.rdhub.util.RDHubConstancts;
import com.rad.rdhub.validator.RDCommonValidator;

@RestController
@RefreshScope
@RequestMapping("/userportal/admin")
public class RDAdminPortalController {

	private static final Logger LOGGER = LogManager.getLogger(RDAdminPortalController.class);
	
	@Autowired
	private RDCache rdCache;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RDCommonValidator validator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ResponseEntity<?> addUser(
			@RequestParam Map<String, String> paramMap, 
			@RequestBody RDUserDTO user,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		
		LOGGER.info("Request : User instance save by " + paramMap.get("username"));
		
		List<String> messages = validator.validateUser(user, RDHubConstancts.RECORD_ADD);
		if (null == messages) {
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
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}
	
	@RequestMapping(value = "/updateUser", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUser(
			@RequestParam Map<String, String> paramMap,
			@RequestBody RDUserDTO user,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		
		LOGGER.info("Request : User instance updated by " + paramMap.get("username"));
		
		List<String> messages = validator.validateUser(user, RDHubConstancts.RECORD_UPDATE);
		if (null == messages) {
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
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}
	
	@RequestMapping(value = "/addRolesToUser", method = RequestMethod.POST)
	public ResponseEntity<?> addUserRole(
			@RequestParam Map<String, String> paramMap,
			@RequestBody RDUserRoleDTO userRole,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		
		LOGGER.info("Request : Roles are added by " + paramMap.get("username") + " to " + userRole.getUserDid());
		
		List<String> messages = validator.validateUserRoles(userRole);
		if (null == messages) {
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/admin/addRolesToUser"
					+ "?username=" + paramMap.get("username");

			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(userRole, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, responseType);
			messages = resEntity.getBody();
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}
	
	@RequestMapping(value = "/revokeRolesFromUser", method = RequestMethod.DELETE)
	public ResponseEntity<?> revokeRolesFromUser(
			@RequestParam Map<String, String> paramMap,
			@RequestBody RDUserRoleDTO userRole,
			@RequestHeader(name = "token", required = true) String token) throws Exception {
		
		LOGGER.info("Request : Roles revoke by " + userRole.getUserDid() + " from " + paramMap.get("username"));
		
		List<String> messages = validator.validateUserRoles(userRole);
		if (null == messages) {
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/admin/revokeRolesFromUser"
					+ "?username=" + paramMap.get("username");

			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(userRole, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.DELETE, entity, responseType);
			messages = resEntity.getBody();
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}

	@RequestMapping(value = "/createLoginAccountByAdmin", method = RequestMethod.POST)
	public ResponseEntity<?> createLoginAccountByAdmin(
			@RequestParam Map<String, String> paramMap,
			@RequestBody RDLoginDTO loginDTO,
			@RequestHeader(value = "token", required = true) String token) throws Exception {
		
		LOGGER.info("Request : Create login account to " + loginDTO.getUsername() + " by " + paramMap.get("username"));
		
		List<String> messages = validator.validateLoginAccount(loginDTO, RDHubConstancts.RECORD_ADD);
		if (null == messages) {
			loginDTO.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
			loginDTO.setConfimPwd(passwordEncoder.encode(loginDTO.getConfimPwd()));
			
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/user/createLoginAccountByAdmin"
					+ "?username=" + paramMap.get("username");

			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(loginDTO, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, responseType);
			messages = resEntity.getBody();
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}
	
	@RequestMapping(value = "/loginAccountUpdateStatusByAdmin", method = RequestMethod.PUT)
	public ResponseEntity<?> loginAccountUpdateStatusByAdmin(
			@RequestParam Map<String, String> paramMap,
			@RequestBody RDLoginDTO loginDTO,
			@RequestHeader(value = "token", required = true) String token) throws Exception {
		
		LOGGER.info("Request : Update status of login account of " + loginDTO.getUserDid() + " by " + paramMap.get("username"));
		
		List<String> messages = validator.validateBeforeStatusUpdateOfLoginAccount(loginDTO);
		if (null == messages) {
			String url = rdCache.getUserPortalInfo()
					+ "/userportal/admin/loginAccountUpdateStatusByAdmin"
					+ "?username=" + paramMap.get("username");

			HttpHeaders httpHeader = new HttpHeaders();
			httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			httpHeader.set("Authorization", "Bearer " + token);
			HttpEntity<Object> entity = new HttpEntity<Object>(loginDTO, httpHeader);
			
			ParameterizedTypeReference<List<String>> responseType = new ParameterizedTypeReference<List<String>>() {};
			ResponseEntity<List<String>> resEntity = restTemplate.exchange(url.toString(), HttpMethod.PUT, entity, responseType);
			messages = resEntity.getBody();
			return ResponseEntity.status(HttpStatus.OK).body(messages.toString());

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messages.toString());
		}
	}
}
