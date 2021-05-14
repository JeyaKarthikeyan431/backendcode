package com.erp.farwood.portal.dto;

import java.util.List;

import lombok.Data;

@Data
public class ListOptionRequest {

	private List<String> multipleOptionType;

	private String optionType;

	private String level1Id;

	private String level2Id;

	private String level1Type;

	private String level2Type;
}
