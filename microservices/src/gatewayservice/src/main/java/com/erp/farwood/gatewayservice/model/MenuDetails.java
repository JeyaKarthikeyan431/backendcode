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
@Table(name = "UDS_MENU_DTLS")
public class MenuDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UMD_MENU_ID")
	private String menuId;

	@Column(name = "UMD_MENU_NAME")
	private String menuName;

	@Column(name = "UMD_SUB_MENU_ID")
	private String subMenuId;

	@Column(name = "UMD_SUB_MENU_NAME")
	private String subMenuName;

	@Column(name = "UMD_DESCRIPTION")
	private String description;

	@Column(name = "UMD_CREATED_BY")
	private String createdBy;

	@Column(name = "UMD_CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "UMD_UPDATED_BY")
	private String updatedBy;

	@Column(name = "UMD_UPDATED_DATE")
	private LocalDateTime updatedDate;
}
