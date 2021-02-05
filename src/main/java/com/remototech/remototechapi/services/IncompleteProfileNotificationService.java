package com.remototech.remototechapi.services;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Email;
import com.remototech.remototechapi.entities.JobTemplateType;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.MailTemplate;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IncompleteProfileNotificationService {

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private MailTemplateService mailTemplateService;

	@Autowired
	private TemplateProcessorService templateProcessorService;

	@Autowired
	private EmailService emailService;

	public void processIncompleteProfile() throws TemplateException, IOException {

		MailTemplate mailTemplate = mailTemplateService.findByJobTemplateType( JobTemplateType.INCOMPLETE_PROFILE );

		if (mailTemplate == null) {
			log.warn( " +-+-+-+-+-+-+-+ SEM TEMPLATE PARA PROCESSAR" );
		} else {

			String design = mailTemplate.getHtml();

			List<Candidate> candidates = candidateService.findCandidatesWithIncompleteProfile();

			for (Candidate candidate : candidates) {
				Login login = candidate.getLogin();

				if (login == null) {
					log.warn( " --------- Candidato com id " + candidate.getUuid() + " não possui um login vínculado" );
				} else {
					String mailBody = templateProcessorService.processTemplate( design, login.getUser() );

					Email email = Email.builder()
							.text( mailBody )
							.subject( "Parece que seu perfil está incompleto..." )
							.destinatary( login.getEmail() )
							.sender( "contato@remoto.club" )
							.build();

					emailService.queueToSend( email );
				}
			}
		}
	}

}
