package com.remototech.remototechapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class MainClassTest {

	public static void main(String[] args) throws IOException, TemplateException {
		Configuration cfg = new Configuration( Configuration.VERSION_2_3_29 );
		cfg.setDefaultEncoding( "UTF-8" );
		cfg.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
		cfg.setLogTemplateExceptions( false );
		cfg.setWrapUncheckedExceptions( true );
		cfg.setFallbackOnNullLoopVariable( false );

		Product latest = new Product();
		latest.setUrl( "products/greenmouse.html" );
		latest.setName( "green mouse" );

		String templateString = "<html>\r\n"
				+ "<head>\r\n"
				+ "  <title>Welcome!</title>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "  <p>Our latest product:\r\n"
				+ "  <a href=\"${url}\">${name}</a>!\r\n"
				+ "</body>\r\n"
				+ "</html>";

		/* Get the template (uses cache internally) */
		Template temp = new Template( "Teste", new StringReader( templateString ), cfg );

		/* Merge data-model with template */
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter( byteArrayOutputStream );
		temp.process( latest, out );
		System.out.println( new String( byteArrayOutputStream.toByteArray() ) );
		// Note: Depending on what `out` is, you may need to call `out.close()`.
		// This is usually the case for file output, but not for servlet output.
	}

}
