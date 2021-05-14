package com.erp.farwood.gatewayservice.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "UDS_ROLE_GROUPS")
public class RoleGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "URG_MENU_ID")
	private String menuId;

	@Column(name = "URG_USER_ROLE")
	private String userRole;

	@Column(name = "URG_SUB_MENU_ID")
	private String subMenuId;

	@Column(name = "URG_GROUP_ID")
	private String groupId;

	@Column(name = "URG_CATEGORY")
	private String category;

	@Column(name = "URG_DESCRIPTION")
	private String description;

	@Column(name = "URG_CREATED_BY")
	private String createdBy;

	@Column(name = "URG_CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "URG_UPDATED_BY")
	private String updatedBy;

	@Column(name = "URG_UPDATED_DATE")
	private LocalDateTime updatedDate;
}
