package com.erp.farwood.gatewayservice.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * A container class to store and retrieve informations from database tables.
 * This include token details with token expire time limit range.
 *
 */

@Entity
@Table(name = "UDS_TOKEN_DTLS")
@Data

public class TokenDetail implements Serializable {

	private static final long serialVersionUID = 6576324953564602096L;

	@Id
	@Column(name = "UTD_TOKEN_ID", updatable = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer tokenId;

	@Column(name = "UTD_USER_ID")
	private Integer userId;

	@Column(name = "UTD_TOKEN")
	private String token;

	@Column(name = "UTD_IS_REMEMBER")
	private Integer isRemember;

	@Column(name = "UTD_DEVICE_TYPE")
	private String devicetype;

	@Column(name = "UTD_CLIENT_IP")
	private String clientIp;

	@Column(name = "UTD_TOKEN_EXPIRY_TIME")
	private LocalDateTime tokenExpiryTime;

	@Column(name = "UTD_CREATED_BY")
	private String creadtedBy;

	@Column(name = "UTD_CREATED_DATE")
	private LocalDateTime creadtedDate;

	@Column(name = "UTD_UPDATED_BY")
	private String updatedBy;

	@Column(name = "UTD_UPDATED_DATE")
	private LocalDateTime updatedDate;

}
