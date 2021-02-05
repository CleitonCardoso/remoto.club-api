package com.remototech.remototechapi.services;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remototech.remototechapi.entities.Email;
import com.remototech.remototechapi.entities.EmailStatus;
import com.remototech.remototechapi.repositories.EmailRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private EmailRepository emailRepository;

	public void sendSimpleMessage(String from, String to, String subject, String text) throws MessagingException {
		if (emailSender != null) {

			MimeMessage message = emailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper( message, true );

			helper.setTo( to );
			helper.setFrom( from );
			helper.setSubject( subject );
			helper.setText( text, true );

			message.setSender( new InternetAddress( from ) );

			emailSender.send( message );
		} else {
			log.info( "Email não enviado, serviço de email inválido. Confira se existe alguma configuração de email válida e ativa no banco de dados." );
		}
	}

	public void sendMessageWithAttachment(String from, String to, String subject, String text, byte[] attachment,
			String attachmentTitle) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper( message, true );

		helper.setTo( to );
		helper.setFrom( from );
		helper.setSubject( subject );
		helper.setText( text );

		InputStreamResource inputStreamResource = new InputStreamResource( new ByteArrayInputStream( attachment ) );
		helper.addAttachment( attachmentTitle, inputStreamResource );

		emailSender.send( message );
	}

	public void queueToSend(Email email) {
		email.setStatus( EmailStatus.QUEUED );
		emailRepository.save( email );
	}

	public List<Email> listQueuedEmails() {
		return emailRepository.findAllByStatus( EmailStatus.QUEUED );
	}

	@Async
	@Transactional
	public void send(Email email) {
		try {
			sendSimpleMessage( email.getSender(), email.getDestinatary(), email.getSubject(), email.getText() );
			email.setStatus( EmailStatus.SENT );
		} catch (Exception e) {
			log.error( "Email não enviado", e );
			email.setStatus( EmailStatus.ERROR );
		}
		emailRepository.save( email );
	}

}
