package com.erp.farwood.gatewayservice.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDto {

	private Integer userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNo;
	private String role;
	private String department;
	private String groupId;
	private String forcePwdChange;
	private String userType;
	private String userStatus;
	private String roleDesc;
	private String departmentDesc;
	private String createdBy;
	private LocalDateTime createdDate;
}