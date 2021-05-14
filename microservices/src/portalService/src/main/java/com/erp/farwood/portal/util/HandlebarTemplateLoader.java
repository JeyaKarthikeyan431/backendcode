package com.erp.farwood.portal.util;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

@Component
public class HandlebarTemplateLoader {

	private Handlebars handlebars;

	public Template getTemplate(String templateName) throws IOException {
		return this.handlebars.compile(templateName);
	}
}
