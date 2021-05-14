package com.erp.farwood.portal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("app")
public class AppProperties {

	private String dateFormat;
	private String AESKey;
	private String sharedDocumentPath;
	private String documentTemplatePath;
	private String uploadDocumentPath;
	private String imageConvertedPath;

}
