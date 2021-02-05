package com.remototech.remototechapi.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Email;
import com.remototech.remototechapi.entities.JobTemplateType;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.MailTemplate;
import com.remototech.remototechapi.entities.Role;

import freemarker.template.TemplateException;

@Service
public class AccountCreationNotificationService {

	@Autowired
	private TemplateProcessorService templateProcessorService;

	@Autowired
	private MailTemplateService mailTemplateService;

	@Autowired
	private EmailService emailService;

	@Async
	public void notify(Login login) throws TemplateException, IOException {
		Role role = login.getRole();

		MailTemplate template = null;

		if (Role.CANDIDATE.equals( role )) {
			template = mailTemplateService.findByJobTemplateType( JobTemplateType.CANDIDATE_ACCOUNT_SUCCESS );
		} else {
			template = mailTemplateService.findByJobTemplateType( JobTemplateType.RECRUITER_ACCOUNT_SUCCESS );
		}

		String mailContent = templateProcessorService.processTemplate( template.getHtml(), login );

		Email email = Email.builder()
				.text( mailContent )
				.subject( "Remoto.Club - Que bom ter você com a gente!" )
				.destinatary( login.getEmail() )
				.sender( "contato@remoto.club" )
				.build();
		emailService.queueToSend( email );
	}

}
