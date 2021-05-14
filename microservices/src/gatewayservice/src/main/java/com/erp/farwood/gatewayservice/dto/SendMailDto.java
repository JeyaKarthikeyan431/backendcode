package com.erp.farwood.gatewayservice.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SendMailDto {

	private List<String> toEmail;

	private List<String> cc;

	private List<String> bcc;

	@NotNull(message = "Subject Cannot be Empty")
	private String subject;

	@NotNull(message = "Message Cannot be Empty")
	private String message;

	private Integer userId;

	private List<Attachment> attachments = new ArrayList<>();

	private String contentType;

	private boolean sendToMailingList = false;

	private boolean displayEmailSignature = false;

	public void addAttachment(Attachment attachment) {
		this.attachments.add(attachment);
	}
}
