package com.rad.rdhub.dto;

import java.io.Serializable;

public class RDUserDTO extends RDAbstractDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstName;
	private char gender;
	private String lastName;
	private String middleName;

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

}
