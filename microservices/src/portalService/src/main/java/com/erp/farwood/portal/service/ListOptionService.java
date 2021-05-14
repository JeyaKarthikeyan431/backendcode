package com.erp.farwood.portal.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.erp.farwood.portal.dto.LabelValueDto;
import com.erp.farwood.portal.dto.ListOptionRequest;
import com.erp.farwood.portal.dto.ListParentDto;
import com.erp.farwood.portal.exception.NoDataFoundException;
import com.erp.farwood.portal.model.ListOptionsMaster;
import com.erp.farwood.portal.repository.ListOptionsRepo;
import com.erp.farwood.portal.response.ListResponse;
import com.erp.farwood.portal.response.Response;
import com.erp.farwood.portal.util.MotorInsuranceConstants;

@Service
public class ListOptionService {

	@Autowired
	private ListOptionsRepo listOptionsRepo;

	@Transactional(readOnly = true)
	public Response<ListParentDto> findAllOptions(ListOptionRequest listOptionRequest) {

		Response<ListParentDto> response = new Response<ListParentDto>();
		ListParentDto listParent = new ListParentDto();
		List<LabelValueDto> nationality = new LinkedList<>();
		List<LabelValueDto> gender = new LinkedList<>();
		List<LabelValueDto> bankName = new LinkedList<>();
		List<LabelValueDto> userDesignation = new LinkedList<>();
		List<LabelValueDto> userDepartment = new LinkedList<>();
		List<LabelValueDto> workLocation = new LinkedList<>();
		List<LabelValueDto> maritalStatus = new LinkedList<>();
		List<LabelValueDto> commuteMode = new LinkedList<>();
		List<LabelValueDto> employeeQualification = new LinkedList<>();
		List<LabelValueDto> idType = new LinkedList<>();
		List<LabelValueDto> relationship = new LinkedList<>();
		List<LabelValueDto> questionType = new LinkedList<>();
		List<LabelValueDto> year = new LinkedList<>();
		List<LabelValueDto> occupation = new LinkedList<>();
		List<LabelValueDto> customerType = new LinkedList<>();
		List<LabelValueDto> businessSource = new LinkedList<>();
		List<LabelValueDto> leadStatus = new LinkedList<>();
		List<LabelValueDto> propertyType = new LinkedList<>();
		List<LabelValueDto> srcLookingFor = new LinkedList<>();
		List<LabelValueDto> srcDoc = new LinkedList<>();
		List<LabelValueDto> moduleName = new LinkedList<>();

		ArrayList<String> sortByAscVal = new ArrayList<String>();
		sortByAscVal.add("NATIONALITY");
		sortByAscVal.add("GENDER");
		sortByAscVal.add("BANK_NAME");
		sortByAscVal.add("USER_DESIGNATION");
		sortByAscVal.add("USER_DEPT");
		sortByAscVal.add("WRK_LOC");
		sortByAscVal.add("MARITAL_STATUS");
		sortByAscVal.add("COMMUTE_MODE");
		sortByAscVal.add("EMP_QUALIFICATION");
		sortByAscVal.add("ID_TYP");
		sortByAscVal.add("RELATIONSHIP");
		sortByAscVal.add("QUESTION_TYPE");
		sortByAscVal.add("YEAR");
		sortByAscVal.add("OCCUPATION");
		sortByAscVal.add("CUST_TYP");
		sortByAscVal.add("BUS_SRC");
		sortByAscVal.add("LEAD_STATUS");
		sortByAscVal.add("PROP_TYPE");
		sortByAscVal.add("SRC_LOK_FOR");
		sortByAscVal.add("SRC_DOC");
		sortByAscVal.add("MODULE_NAME");

		Stream<ListOptionsMaster> options = null;
		try {
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(0))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(0));
				options.forEach(values -> nationality.add(getOptions(values)));
				listParent.setNationality(nationality);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(1))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(1));
				options.forEach(values -> gender.add(getOptions(values)));
				listParent.setGender(gender);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(2))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(2));
				options.forEach(values -> bankName.add(getOptions(values)));
				listParent.setBankName(bankName);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(3))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(3));
				options.forEach(values -> userDesignation.add(getOptions(values)));
				listParent.setUserDesignation(userDesignation);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(4))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(4));
				options.forEach(values -> userDepartment.add(getOptions(values)));
				listParent.setUserDepartment(userDepartment);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(5))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(5));
				options.forEach(values -> workLocation.add(getOptions(values)));
				listParent.setWorkLocation(workLocation);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(6))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(6));
				options.forEach(values -> maritalStatus.add(getOptions(values)));
				listParent.setMaritalStatus(maritalStatus);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(7))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(7));
				options.forEach(values -> commuteMode.add(getOptions(values)));
				listParent.setCommuteMode(commuteMode);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(8))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(8));
				options.forEach(values -> employeeQualification.add(getOptions(values)));
				listParent.setEmployeeQualification(employeeQualification);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(9))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(9));
				options.forEach(values -> idType.add(getOptions(values)));
				listParent.setIdType(idType);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(10))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(10));
				options.forEach(values -> relationship.add(getOptions(values)));
				listParent.setRelationship(relationship);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(11))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(11));
				options.forEach(values -> questionType.add(getOptions(values)));
				listParent.setQuestionType(questionType);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(12))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(12));
				options.forEach(values -> year.add(getOptions(values)));
				listParent.setYear(year);
			}

			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(13))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(13));
				options.forEach(values -> occupation.add(getOptions(values)));
				listParent.setOccupation(occupation);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(14))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(14));
				options.forEach(values -> customerType.add(getOptions(values)));
				listParent.setCustomerType(customerType);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(15))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(15));
				options.forEach(values -> businessSource.add(getOptions(values)));
				listParent.setBusinessSource(businessSource);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(16))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(16));
				options.forEach(values -> leadStatus.add(getOptions(values)));
				listParent.setLeadStatus(leadStatus);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(17))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(17));
				options.forEach(values -> propertyType.add(getOptions(values)));
				listParent.setPropertyType(propertyType);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(18))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(18));
				options.forEach(values -> srcLookingFor.add(getOptions(values)));
				listParent.setSrcLookingFor(srcLookingFor);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(19))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(19));
				options.forEach(values -> srcDoc.add(getOptions(values)));
				listParent.setSrcDoc(srcDoc);
			}
			if (listOptionRequest.getMultipleOptionType().contains(sortByAscVal.get(20))) {
				options = listOptionsRepo.findByListOptionsByIdType(sortByAscVal.get(20));
				options.forEach(values -> moduleName.add(getOptions(values)));
				listParent.setModuleName(moduleName);
			}
			response.setData(listParent);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setMessage(MotorInsuranceConstants.AVAILABLE);
			options.close();
		} catch (Exception e) {
			throw new NoDataFoundException(MotorInsuranceConstants.USER_NOT_FOUND);
		}
		return response;
	}

	@Transactional(readOnly = true)
	public ListResponse<List<LabelValueDto>> findOptions(ListOptionRequest listOptionRequest) {
		ListResponse<List<LabelValueDto>> response = new ListResponse<List<LabelValueDto>>();
		List<LabelValueDto> results = new LinkedList<>();

		Stream<ListOptionsMaster> options = null;
		if (!ObjectUtils.isEmpty(listOptionRequest) && !StringUtils.isEmpty(listOptionRequest.getOptionType())) {
			if (listOptionRequest.getLevel1Id() != null && listOptionRequest.getLevel2Id() != null
					&& listOptionRequest.getLevel1Type() != null && listOptionRequest.getLevel2Type() != null) {

//				String level1Value = listOptionsRepo.findValueByTypeAndId(listOptionRequest.getLevel1Type(),
//						listOptionRequest.getLevel1Id());
//				String level2Value = listOptionsRepo.findValueByTypeAndId(listOptionRequest.getLevel2Type(),
//						listOptionRequest.getLevel2Id());

				options = listOptionsRepo.findListOptionsByLevel1AndLevel2TypeValue(listOptionRequest.getOptionType(),
						listOptionRequest.getLevel1Type(), listOptionRequest.getLevel1Id(),
						listOptionRequest.getLevel2Type(), listOptionRequest.getLevel2Id());
				options.forEach(vc -> results.add(getOptions(vc)));
				if (!CollectionUtils.isEmpty(results)) {
					response.setData(results);
					response.setStatus(HttpServletResponse.SC_OK);
					response.setMessage(MotorInsuranceConstants.AVAILABLE);
					options.close();
				} else {
					response.setStatus(HttpServletResponse.SC_NO_CONTENT);
					response.setMessage(MotorInsuranceConstants.NOT_AVAILABLE);
				}
			} else if (listOptionRequest.getLevel1Id() != null && listOptionRequest.getLevel1Type() != null) {
//				String level1Value = listOptionsRepo.findValueByTypeAndId(listOptionRequest.getLevel1Type(),
//						listOptionRequest.getLevel1Id());
				options = listOptionsRepo.findListOptionsByLevel1TypeAndValue(listOptionRequest.getOptionType(),
						listOptionRequest.getLevel1Type(), listOptionRequest.getLevel1Id());
				options.forEach(vc -> results.add(getOptions(vc)));
				if (!CollectionUtils.isEmpty(results)) {
					response.setData(results);
					response.setStatus(HttpServletResponse.SC_OK);
					response.setMessage(MotorInsuranceConstants.AVAILABLE);
					options.close();
				} else {
					response.setStatus(HttpServletResponse.SC_NO_CONTENT);
					response.setMessage(MotorInsuranceConstants.NOT_AVAILABLE);
				}
			} else {
				options = listOptionsRepo.findByListOptionsByIdType(listOptionRequest.getOptionType());
				options.forEach(vc -> results.add(getOptions(vc)));
				if (!CollectionUtils.isEmpty(results)) {
					response.setData(results);
					response.setStatus(HttpServletResponse.SC_OK);
					response.setMessage(MotorInsuranceConstants.AVAILABLE);
					options.close();
				} else {
					response.setStatus(HttpServletResponse.SC_NO_CONTENT);
					response.setMessage(MotorInsuranceConstants.NOT_AVAILABLE);
				}
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			response.setMessage(MotorInsuranceConstants.NOT_AVAILABLE);
		}
		return response;
	}

	private LabelValueDto getOptions(ListOptionsMaster optionsEntity) {
		LabelValueDto option = new LabelValueDto();
		option.setValue(optionsEntity.getOptionId());
		option.setLabel(optionsEntity.getIdValue());
		return option;
	}
}
