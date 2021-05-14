package com.erp.farwood.portal.service;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.erp.farwood.portal.client.NotificationClient;
import com.erp.farwood.portal.dto.Attachment;
import com.erp.farwood.portal.dto.SendMailDto;
import com.erp.farwood.portal.dto.SendPasswordDto;
import com.erp.farwood.portal.exception.NoDataFoundException;
import com.erp.farwood.portal.model.DocumentTemplate;
import com.erp.farwood.portal.repository.DocumentTemplateRepository;
import com.erp.farwood.portal.response.Response;
import com.erp.farwood.portal.util.MotorInsuranceConstants;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

@Service
public class EmailService {

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private DocumentTemplateRepository documentTemplateRepository;

	public Response<String> sendPassword(SendPasswordDto sendPasswordDto) {

		Map<String, Object> data = new HashMap<>();
		data.put("userName", sendPasswordDto.getUserName());
		data.put("password", sendPasswordDto.getPassword());
//		data.put("isInlineImageVisible", "block");
		Map<String, String> mailContent = getEmailContentByTemplate("FORGET_PASSWORD", data);
		sendMail(mailContent, null, sendPasswordDto.getEmailId());

		Response<String> response = new Response<>();
		response.setMessage(MotorInsuranceConstants.SUCCESS);
		response.setStatus(HttpStatus.OK.value());
		return response;
	}

	public void sendMail(Map<String, String> mailContent, List<Attachment> attachments, String toAddresses,
			String... ccMailAddresses) {
		SendMailDto mailDto = convertToMailDto(mailContent, attachments, false, toAddresses, ccMailAddresses);
		notificationClient.sendMail(mailDto);
	}

	private SendMailDto convertToMailDto(Map<String, String> mailContent, List<Attachment> attachments,
			boolean sendToMailingList, String toAddresses, String... cc) {
		notNull(mailContent, "Mail content is required");
		SendMailDto mailDto = new SendMailDto();
		mailDto.setDisplayEmailSignature(false);
		mailDto.setSendToMailingList(sendToMailingList);
		mailDto.setSubject(mailContent.get("SUBJECT"));
		mailDto.setMessage(mailContent.get("BODY"));
		mailDto.setAttachments(attachments);
		mailDto.setToEmail(Collections.singletonList(toAddresses));
		mailDto.setDisplayEmailSignature(true);
		if (cc != null)
			mailDto.setCc(Arrays.asList(cc));
		return mailDto;
	}

	public Map<String, String> getEmailContentByTemplate(String templateName, Map<String, Object> data) {
		notNull(templateName, "Template name is required");
		Map<String, String> mailContent = new HashMap<>();
		try {

			Optional<DocumentTemplate> optionalTemplate = documentTemplateRepository.findDocumentTemplate(templateName,
					"MAIL");
			DocumentTemplate docTemplate = optionalTemplate
					.orElseThrow(() -> new NoDataFoundException("Template " + templateName + " not found"));

			TemplateLoader loader = new ClassPathTemplateLoader("/templates", ".hbs");
			Handlebars handlebars = new Handlebars(loader);
			Template template = handlebars.compile(templateName);
			String body = template.apply(data);

			String subject = docTemplate.getDisplayName();
			if (subject.contains("<POLICY_NO>"))
				subject = StringUtils.replace(subject, "<POLICY_NO>", data.get("policyNo").toString());
			if (subject.contains("<QUOTE_NO>"))
				subject = StringUtils.replace(subject, "<QUOTE_NO>", data.get("quoteNo").toString());
			if (subject.contains("<OTP>"))
				subject = StringUtils.replace(subject, "<OTP>", data.get("OTP").toString());
			mailContent.put("SUBJECT", subject);
			mailContent.put("BODY", body);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mailContent;
	}

}
