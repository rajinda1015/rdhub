package com.rad.rdhub.dto;

import java.io.Serializable;

public class RDUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userDid;
	private String firstName;
	private char gender;
	private String lastName;
	private String middleName;
	private byte status;

	public Long getUserDid() {
		return userDid;
	}

	public void setUserDid(Long userDid) {
		this.userDid = userDid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}
