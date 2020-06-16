package com.rad.rdhub.dto;

import java.io.Serializable;

public class RDLoginDTO extends RDUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private String confimPwd;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfimPwd() {
		return confimPwd;
	}

	public void setConfimPwd(String confimPwd) {
		this.confimPwd = confimPwd;
	}
	
}
