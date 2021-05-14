package com.erp.farwood.portal.dto;

import lombok.Data;

@Data
public class SendPasswordDto {

	private String userName;

	private String password;

	private String emailId;

}
