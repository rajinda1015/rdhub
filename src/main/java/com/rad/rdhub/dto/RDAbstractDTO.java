package com.rad.rdhub.dto;

import java.io.Serializable;
import java.util.Date;

public abstract class RDAbstractDTO implements Serializable {

	private static final long serialVersionUID = 2L;
	private Long did;
	private Date createDate;
	private byte status;

	public Long getDid() {
		return did;
	}

	public void setDid(Long did) {
		this.did = did;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
}
