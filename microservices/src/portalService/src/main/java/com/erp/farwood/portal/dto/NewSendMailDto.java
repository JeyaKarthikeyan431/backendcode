package com.erp.farwood.portal.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewSendMailDto {

	@NotNull(message = "To Address Cannot be Empty")
	private List<String> toEmail;

	@NotNull(message = "Subject Cannot be Empty")
	private String subject;

	@NotNull(message = "Message Cannot be Empty")
	private String message;

	@NotNull(message = "UserName Cannot be Empty")
	private String userName;

	private String contentType;

	private String attachmentFile;

	private String attachmentFileName;

	public NewSendMailDto(List<String> toEmail, String subject, String message, String userName, String contentType) {
		super();
		this.toEmail = toEmail;
		this.subject = subject;
		this.message = message;
		this.userName = userName;
		this.contentType = contentType;
	}

}
