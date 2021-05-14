package com.erp.farwood.portal.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class OfficialInfo {

	private String offEmpId;
	private LocalDate offEmpDateOfJoining;
	private String offEmpDesignation;
	private String offEmpDesignationDesc;
	private String offEmpDepartment;
	private String offEmpDepartmentDesc;
	private String offEmpReportingTo;
	private String offEmpWorkLocation;
	private String offEmpWorkLocationDesc;
	private String offEmpPhoto;
	private String offEmpOffContactNo;
	private String offEmpOfficialEmail;

}
