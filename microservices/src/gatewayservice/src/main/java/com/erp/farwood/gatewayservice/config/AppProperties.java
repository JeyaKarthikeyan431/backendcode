package com.erp.farwood.gatewayservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app")
public class AppProperties {

	private String companyId;
	private String AESKey;
	private Integer loginAttemptCount;

}
