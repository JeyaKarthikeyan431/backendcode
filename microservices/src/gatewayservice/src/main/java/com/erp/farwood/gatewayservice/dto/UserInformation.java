package com.erp.farwood.gatewayservice.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class UserInformation {

	private String userName;

	private String userType;

	private Integer userId;

//	private String fullName;

	private String firstName;

	private String lastName;

	private String token;

	private String email;

	private String role;

	private String groupId;

	private String department;

	private String lastLoggedIn;

	private String forcePwdChange;

	private String isAlreadyLoggedIn = "N";

	private LocalDate lastPwdChangeDate;

	private String roleDesc;

	private String departmentDesc;

	private List<MenuDto> menus;
}
