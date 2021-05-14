package com.erp.farwood.portal.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "UDS_USER_LOGIN_DTLS")
public class LoginDetails implements Serializable {

	private static final long serialVersionUID = -8302863140489934703L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ULD_USER_ID")
	private Integer userId;

	@Column(name = "ULD_USER_NAME")
	private String userName;

	@Column(name = "ULD_PASSWORD")
	private String password;

	@Column(name = "ULD_CREATED_BY")
	private String createdBy;

	@Column(name = "ULD_CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "ULD_UPDATED_BY")
	private String updatedBy;

	@Column(name = "ULD_UPDATED_DATE")
	private LocalDateTime updatedDate;

	@Column(name = "ULD_FORCE_PWD_CHANGE")
	private String forcePwdChange;

	@Column(name = "ULD_USER_TYPE")
	private String userType;

	@Column(name = "ULD_STATUS")
	private String userStatus;

	@Column(name = "ULD_LAST_LOGGED_IN")
	private LocalDateTime lastLoggedIn;

	@Column(name = "ULD_EMAIL_ID")
	private String emailId;

	@Column(name = "ULD_ROLE")
	private String role;

	@Column(name = "ULD_DEPARTMENT")
	private String department;

	@Column(name = "ULD_GROUP_ID")
	private String groupId;

	@Column(name = "ULD_LAST_PWD_CH_DATE")
	private LocalDate lastPwdChangeDate;

	@Column(name = "ULD_FIRST_NAME")
	private String firstName;

	@Column(name = "ULD_LAST_NAME")
	private String lastName;

	@Column(name = "ULD_MOBILE_NO")
	private String mobileNo;

	@Column(name = "ULD_PW_PROFILE")
	private String passwordProfile;

	@Override
	public String toString() {
		return "LoginDetails [userId=" + userId + ", userName=" + userName + ", password=" + password + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", updatedDate="
				+ updatedDate + ", forcePwdChange=" + forcePwdChange + ", userType=" + userType + ", userStatus="
				+ userStatus + ", lastLoggedIn=" + lastLoggedIn + ", emailId=" + emailId + ", role=" + role
				+ ", department=" + department + ", groupId=" + groupId + ", lastPwdChangeDate=" + lastPwdChangeDate
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", mobileNo=" + mobileNo
				+ ", passwordProfile=" + passwordProfile + "]";
	}

}
