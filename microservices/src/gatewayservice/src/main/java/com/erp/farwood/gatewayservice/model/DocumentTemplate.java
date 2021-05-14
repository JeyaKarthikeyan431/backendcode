package com.erp.farwood.gatewayservice.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "UDS_DOC_TEMPLATE")
public class DocumentTemplate implements Serializable {

	private static final long serialVersionUID = -8302863140489934703L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "UDT_ID")
	private Integer id;

	@Column(name = "UDT_TMPL_NAME")
	private String templateName;

	@Column(name = "UDT_TMPL_DESCRIPTION")
	private String templateDescription;

	@Column(name = "UDT_TMPL_TYP")
	private String templateType;

	@Column(name = "UDT_DISPLAY_NAME")
	private String displayName;

	@Column(name = "UDT_CREATED_BY")
	private String createdBy;

	@Column(name = "UDT_CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "UDT_UPDATED_BY")
	private String updatedBy;

	@Column(name = "UDT_UPDATED_DATE")
	private LocalDateTime updatedDate;

}
