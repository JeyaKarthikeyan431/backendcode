package com.erp.farwood.gatewayservice.dto;

import lombok.Data;

@Data
public class SendPasswordDto {

	private String userName;

	private String password;

	private String emailId;
}
