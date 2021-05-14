package com.erp.farwood.gatewayservice.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/test")
public class TestController {

	@GetMapping
	public String getWish() {

		return "Hello, how are you? iam from gateway service";
	}

}
