package com.erp.farwood.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.farwood.portal.dto.LabelValueDto;
import com.erp.farwood.portal.dto.ListOptionRequest;
import com.erp.farwood.portal.dto.ListParentDto;
import com.erp.farwood.portal.response.ListResponse;
import com.erp.farwood.portal.response.Response;
import com.erp.farwood.portal.service.ListOptionService;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/options")
public class ListOptionController {

	@Autowired
	private ListOptionService listOptionService;

	@PostMapping("/listAll")
	public Response<ListParentDto> findAllOptions(@RequestBody ListOptionRequest listOptionRequest) {

		return listOptionService.findAllOptions(listOptionRequest);
	}

	@PostMapping("/list")
	public ListResponse<List<LabelValueDto>> findOptions(@RequestBody ListOptionRequest listOptionRequest) {

		return listOptionService.findOptions(listOptionRequest);
	}

}
