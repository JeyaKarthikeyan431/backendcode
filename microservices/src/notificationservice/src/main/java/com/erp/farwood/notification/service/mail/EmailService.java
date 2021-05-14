package com.erp.farwood.notification.service.mail;

import com.erp.farwood.notification.dto.SendMailDto;

public interface EmailService {
	
	void sendMail(SendMailDto mailDto);

}
