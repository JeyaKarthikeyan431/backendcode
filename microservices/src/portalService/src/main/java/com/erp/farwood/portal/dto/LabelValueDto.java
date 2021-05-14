package com.erp.farwood.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelValueDto implements Comparable<LabelValueDto> {

	private String label;
	private String value;

	@Override
	public int compareTo(LabelValueDto o) {
		return o.getValue().compareTo(getLabel());
	}
}
