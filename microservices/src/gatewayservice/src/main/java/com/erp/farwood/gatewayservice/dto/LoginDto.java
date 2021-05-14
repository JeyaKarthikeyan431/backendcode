package com.erp.farwood.gatewayservice.dto;

import javax.validation.constraints.NotNull;

@lombok.Data
public class LoginDto {

	@NotNull(message = "User email cannot be Empty")
	private String emailId;

	@NotNull(message = "Password cannot be Empty")
	private String password;

}
