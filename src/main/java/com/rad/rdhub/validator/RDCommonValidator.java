package com.rad.rdhub.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rad.rdhub.dto.RDContactDTO;
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

		if (action == RDHubConstancts.RECORD_ADD && (null != user.getDid() && user.getDid() > 0)) {
			messageList = new ArrayList<String>();
			messageList.add("Cannot insert existing user again");
		}

		if (action == RDHubConstancts.RECORD_UPDATE && (null == user.getDid() || user.getDid() == 0)) {
			if (null == messageList) { messageList = new ArrayList<String>(); }
			messageList.add("Please select user before update the details");
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
	
	public List<String> validateLoginAccount(RDLoginDTO loginAccount, int action) throws Exception {
		List<String> messageList = null;
		
		if (action == RDHubConstancts.RECORD_ADD) {
			if (null == loginAccount.getDid() || loginAccount.getDid().longValue() < 0) {
				messageList = new ArrayList<String>();
				messageList.add("Please select user to be created login account");
			}
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
	

	public List<String> validateBeforeStatusUpdateOfLoginAccount(RDLoginDTO loginAccount) throws Exception {
		List<String> messageList = null;
		
		if (null == loginAccount.getDid() || loginAccount.getDid().longValue() < 0) {
			messageList = new ArrayList<String>();
			messageList.add("Please select user before update login account");
		}
		
		return messageList;
	}
	
	public List<String> validateRegisterUser(RDLoginDTO loginDTO) throws Exception {
		List<String> messageListUser = null;
		List<String> messageListAccount = null;
		List<String> messageList = new ArrayList<String>();
		
		messageListUser = validateUser(loginDTO, RDHubConstancts.RECORD_ADD);
		messageListAccount = validateLoginAccount(loginDTO, RDHubConstancts.DEFAULT);
		
		if (null != messageListUser && !messageListUser.isEmpty()) { messageList.addAll(messageListUser); }
		if (null != messageListAccount && !messageListAccount.isEmpty()) { messageList.addAll(messageListAccount); }
		
		if (messageList.size() > 0) {
			return messageList;
		} else {
			return null;
		}
	}
	
	public List<String> validateContacts(RDContactDTO[] contacts, int action) throws Exception {
		List<String> messageList = null;
		
		if (null == contacts || 0 == contacts.length) {
			messageList = new ArrayList<String>();
			messageList.add("Please provide contact list");
		}
		
		if (null != contacts && contacts.length > 0) {
			int contactDids = 0;
			int defaultContacts = 0;
			int msgUserdid = 0;
			int msgType = 0;
			int msgValue = 0;
			for (RDContactDTO contact : contacts) {
				if (action == RDHubConstancts.RECORD_ADD) {
					if (null != contact.getDid() && contact.getDid().longValue() >= 0) {
						++contactDids;
					}
				} else if (action == RDHubConstancts.RECORD_UPDATE) {
					if (null == contact.getDid() || 0 == contact.getDid().longValue()) {
						++contactDids;
					}
				}
				
				if (action == RDHubConstancts.RECORD_ADD) {
					if (null == contact.getUserDid() || contact.getUserDid() <= 0) {
						++msgUserdid;
					}
				}
				
				if (null == contact.getType() || contact.getType().intValue() == 0) {
					++msgType;
				}
				
				if (null == contact.getValue() || contact.getValue().trim().isEmpty()) {
					++msgValue;
				}
				if (contact.getDefaultValue() == RDHubConstancts.CONTACT_DEFAULT) {
					defaultContacts++;
				}
			}
			
			if (contactDids > 0) {
				if (null == messageList) { messageList = new ArrayList<String>(); }
				messageList.add("Invalid contact detail");
			}
			
			if (msgUserdid > 0) {
				if (null == messageList) { messageList = new ArrayList<String>(); }
				messageList.add("User is not provided for contact information");
			}

			if (msgType > 0) {
				if (null == messageList) { messageList = new ArrayList<String>(); }
				messageList.add("Please provide contact type for " + msgType + " contact(s)");
			}
			
			if (msgValue > 0) {
				if (null == messageList) { messageList = new ArrayList<String>(); }
				messageList.add("Please provide contact value for " + msgValue + " contact(s)");
			}
			
			if (defaultContacts > 1) {
				if (null == messageList) { messageList = new ArrayList<String>(); }
				messageList.add("You are not allowed to select more than one default contact");
			}
		}

		return messageList;
	}

	public List<String> validateContactDidsToDelete(Long[] dids) throws Exception {
		List<String> messageList = null;
		
		if (null == dids || 0 == dids.length) {
			messageList = new ArrayList<String>();
			messageList.add("Please provide contact(s) to be deleted");
		}

		return messageList;
	}
	
	public String encodePasswd(String pwd) throws Exception {
		return passwordEncoder.encode(pwd);
	}
}
