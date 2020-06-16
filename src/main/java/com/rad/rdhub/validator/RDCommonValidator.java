package com.rad.rdhub.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rad.rdhub.dto.RDLoginDTO;
import com.rad.rdhub.dto.RDUserDTO;
import com.rad.rdhub.dto.RDUserRoleDTO;
import com.rad.rdhub.util.RDHubConstancts;

@Component
public class RDCommonValidator {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<String> validateUser(RDUserDTO user, int action) throws Exception {
		List<String> messageList = null;

		if (action == RDHubConstancts.USER_ADD && (null != user.getUserDid() && user.getUserDid() > 0)) {
			messageList = new ArrayList<String>();
			messageList.add("Cannot insert existing user again");
		}

		if (action == RDHubConstancts.USER_UPDATE && (null == user.getUserDid() || user.getUserDid() == 0)) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("Please add user details before update the details");
		}

		if (null == user.getFirstName() || user.getFirstName().isEmpty()) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("First Name cannot be an empty field");
		}
		
		if (null == user.getLastName() || user.getLastName().isEmpty()) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("Last Name cannot be an empty field");
		}
		
		Character defaultVal = new Character(Character.MIN_VALUE);
		if (0 == defaultVal.compareTo((Character) user.getGender())) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("Gender cannot be an empty field");
		}
		
		return messageList;
	}
	
	public List<String> validateUserRoles(RDUserRoleDTO userRole) throws Exception {
		List<String> messageList = null;
		
		if (userRole.getUserDid() == 0) {
			messageList = new ArrayList<String>();
			messageList.add("Please select user");
		}
		
		if (userRole.getRoleDid() == 0) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("Please select role");
		}
		
		return messageList;
	}
	
	public List<String> validateLoginAccount(RDLoginDTO loginAccount) throws Exception {
		List<String> messageList = null;
		
		if (null == loginAccount.getUserDid() || loginAccount.getUserDid().longValue() < 0) {
			messageList = new ArrayList<String>();
			messageList.add("Please select user to be created login account");
		}

		if (null == loginAccount.getUsername() || loginAccount.getUsername().trim().isEmpty()) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("Username cannot be empty");
		}
		
		if (null == loginAccount.getPassword() || loginAccount.getPassword().trim().isEmpty()) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("Password cannot be empty");
		}
		
		if (null == loginAccount.getConfimPwd() || loginAccount.getConfimPwd().trim().isEmpty()) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("Confirm password cannot be empty");
		}
		
		if ((null != loginAccount.getPassword() && !loginAccount.getPassword().trim().isEmpty())
				&& (null != loginAccount.getConfimPwd() && !loginAccount.getConfimPwd().trim().isEmpty())
				&& !(loginAccount.getPassword().trim().equals(loginAccount.getConfimPwd().trim()))) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("Password and confirm password are not same");
		}
		
		return messageList;
	}
	
	public String encodePasswd(String pwd) throws Exception {
		return passwordEncoder.encode(pwd);
	}
}
