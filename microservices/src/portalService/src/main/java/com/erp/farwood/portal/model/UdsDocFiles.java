package com.erp.farwood.portal.model;

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
@Table(name = "UDS_DOC_FILES")
public class UdsDocFiles implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UDF_FILE_ID")
	private Long fileId;

	@Column(name = "UDF_USER_ID")
	private Integer userId;

	@Column(name = "UDF_DOC_STATUS_ID")
	private Integer docStatusId;

	@Column(name = "UDF_DOC_ID")
	private String docId;

	@Column(name = "UDF_DOC_PATH")
	private String docPath;

	@Column(name = "UDF_DOC_NAME")
	private String docName;

	@Column(name = "UDF_UPLOADED_BY")
	private String uploadedBy;

	@Column(name = "UDF_UPLOADED_DATE")
	private LocalDateTime uploadedDate;

}
