package com.rad.rdhub.dto;

public class RDContactDTO extends RDAbstractDTO {

	private Long userDid;
	private Integer type;
	private String value;
	private byte defaultValue;

	public Long getUserDid() {
		return userDid;
	}

	public void setUserDid(Long userDid) {
		this.userDid = userDid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public byte getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(byte defaultValue) {
		this.defaultValue = defaultValue;
	}
}
