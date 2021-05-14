package com.erp.farwood.notification.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.farwood.notification.dto.SendMailDto;
import com.erp.farwood.notification.response.Response;
import com.erp.farwood.notification.service.mail.EmailService;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/sendMail")
	public Response<String> sendMail(@RequestBody SendMailDto sendMailDto) {
		Response<String> response = new Response<String>();

		try {
			emailService.sendMail(sendMailDto);
			response.setData("Success");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			response.setData("Unable to send an email");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return response;
	}

}
