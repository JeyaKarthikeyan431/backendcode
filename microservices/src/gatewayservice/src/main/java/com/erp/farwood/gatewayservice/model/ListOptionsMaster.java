package com.erp.farwood.gatewayservice.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "UDS_ID_DEFN")
@Data
@IdClass(ListOptionsPK.class)
public class ListOptionsMaster {

	@Id
	@Column(name = "UID_ID_TYP")
	private String idType;

	@Id
	@Column(name = "UID_ID")
	private String optionId;

	@Column(name = "UID_VALUE")
	private String idValue;

	@Column(name = "UID_LEVEL1_TYPE")
	private String level1Type;

	@Column(name = "UID_LEVEL1_VALUE")
	private String level1Value;

	@Column(name = "UID_LEVEL2_TYPE")
	private String level2Type;

	@Column(name = "UID_LEVEL2_VALUE")
	private String level2Value;

	@Column(name = "UID_ORDER_SEQ")
	private Integer orderIdSeq;

	@Column(name = "UID_DESCRIPTION")
	private String description;

	@Column(name = "UID_CREATED_BY")
	private String createdBy;

	@Column(name = "UID_CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "UID_UPDATED_BY")
	private String updatedBy;

	@Column(name = "UID_UPDATED_DATE")
	private LocalDateTime updatedDate;

}
