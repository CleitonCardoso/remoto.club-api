package com.remototech.remototechapi.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Date;

import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@Service
public class TemplateProcessorService {

	public String processTemplate(String template, Object dataModel) throws TemplateException, IOException {
		Configuration cfg = new Configuration( Configuration.VERSION_2_3_29 );
		cfg.setDefaultEncoding( "UTF-8" );
		cfg.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
		cfg.setLogTemplateExceptions( false );
		cfg.setWrapUncheckedExceptions( true );
		cfg.setFallbackOnNullLoopVariable( false );

		Template temp = new Template( new Date().toInstant().toString(), new StringReader( template ), cfg );

		/* Merge data-model with template */
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter( byteArrayOutputStream );
		temp.process( dataModel, out );

		return new String( byteArrayOutputStream.toByteArray() );
	}
}
