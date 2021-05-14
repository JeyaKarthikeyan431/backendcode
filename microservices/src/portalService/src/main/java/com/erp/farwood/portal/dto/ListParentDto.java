package com.erp.farwood.portal.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class ListParentDto {

	private List<LabelValueDto> nationality = new ArrayList<>();

	private List<LabelValueDto> gender = new ArrayList<>();

	private List<LabelValueDto> bankName = new ArrayList<>();

	private List<LabelValueDto> userDesignation = new ArrayList<>();

	private List<LabelValueDto> userDepartment = new ArrayList<>();

	private List<LabelValueDto> workLocation = new ArrayList<>();

	private List<LabelValueDto> maritalStatus = new ArrayList<>();

	private List<LabelValueDto> commuteMode = new ArrayList<>();

	private List<LabelValueDto> employeeQualification = new ArrayList<>();

	private List<LabelValueDto> idType = new ArrayList<>();

	private List<LabelValueDto> relationship = new ArrayList<>();

	private List<LabelValueDto> questionType = new ArrayList<>();

	private List<LabelValueDto> year = new ArrayList<>();

	List<LabelValueDto> occupation = new ArrayList<>();

	List<LabelValueDto> customerType = new ArrayList<>();

	List<LabelValueDto> businessSource = new ArrayList<>();

	List<LabelValueDto> leadStatus = new ArrayList<>();

	List<LabelValueDto> propertyType = new ArrayList<>();

	List<LabelValueDto> srcLookingFor = new ArrayList<>();

	List<LabelValueDto> srcDoc = new ArrayList<>();
	
	List<LabelValueDto> moduleName = new ArrayList<>();

}
