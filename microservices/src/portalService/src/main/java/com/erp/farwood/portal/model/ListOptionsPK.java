package com.erp.farwood.portal.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ListOptionsPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "UID_ID_TYP")
	private String idType;

//	@Id
	@Column(name = "UID_ID")
	private String optionId;

}
