package com.erp.farwood.gatewayservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.erp.farwood.gatewayservice.dto.SendMailDto;
import com.erp.farwood.gatewayservice.response.Response;

@FeignClient(value = "notificationservice")
public interface NotificationClient {

	@PostMapping("/notification/sendMail")
	Response<String> sendMail(@RequestBody SendMailDto sendMailDto);

}
