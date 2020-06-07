package com.rad.rdhub.validator;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class RDErrorResponse {
	
	private String errorCode;
	
	private String errorMsg;

	public RDErrorResponse(String errCode, String errMsg) {
		super();
		this.errorCode = errCode;
		this.errorMsg = errMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}