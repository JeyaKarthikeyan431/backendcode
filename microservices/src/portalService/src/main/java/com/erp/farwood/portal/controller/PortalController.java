package com.erp.farwood.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.farwood.portal.dto.SendPasswordDto;
import com.erp.farwood.portal.response.Response;
import com.erp.farwood.portal.service.EmailService;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("")
public class PortalController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendPassword")
	public Response<String> sendPassword(@RequestBody SendPasswordDto sendPasswordDto) {
		return emailService.sendPassword(sendPasswordDto);
	}

}
