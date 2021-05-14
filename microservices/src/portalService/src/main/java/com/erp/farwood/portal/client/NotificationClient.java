package com.erp.farwood.portal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.erp.farwood.portal.dto.SendMailDto;
import com.erp.farwood.portal.response.Response;

@FeignClient(value = "notificationservice")
public interface NotificationClient {

	@PostMapping("/notification/sendMail")
	Response<String> sendMail(@RequestBody SendMailDto sendMailDto);

}
