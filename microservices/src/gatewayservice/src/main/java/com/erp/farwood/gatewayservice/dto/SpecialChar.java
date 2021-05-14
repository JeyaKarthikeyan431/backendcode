package com.erp.farwood.gatewayservice.dto;

import org.passay.CharacterData;

public class SpecialChar implements CharacterData {

	private String errorCode;
	private String spcl;

	public SpecialChar(String errorCode, String spcl) {
		super();
		this.errorCode = errorCode;
		this.spcl = spcl;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

	@Override
	public String getCharacters() {
		return spcl;
	}

}
