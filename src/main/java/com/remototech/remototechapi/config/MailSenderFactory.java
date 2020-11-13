package com.remototech.remototechapi.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.remototech.remototechapi.entities.MailConfiguration;
import com.remototech.remototechapi.repositories.MailConfigurationRepository;

@Configuration
public class MailSenderFactory {

	@Autowired
	private MailConfigurationRepository mailConfigurationRepository;

	@Bean
	public JavaMailSender getJavaMailSender() {
		MailConfiguration mailConfiguration = mailConfigurationRepository.findByActiveTrue();

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		if (mailConfiguration != null) {
			mailSender.setHost( mailConfiguration.getSmtpHost() );
			mailSender.setPort( mailConfiguration.getSmtpPort() );

			mailSender.setUsername( mailConfiguration.getSmtpUsername() );
			mailSender.setPassword( mailConfiguration.getSmtpPassword() );

			mailSender.setDefaultEncoding( "UTF-8" );

			Properties props = mailSender.getJavaMailProperties();
			props.put( "mail.transport.protocol", "smtp" );
			props.put( "mail.smtp.auth", "true" );
			props.put( "mail.smtp.starttls.enable", "true" );
			props.put( "mail.smtp.ssl.trust", "smtp.gmail.com" );
		}
		return mailSender;
	}

}
