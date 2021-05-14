package com.erp.farwood.gatewayservice.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {

	private String newPassword;
	private String oldPassword;
}
