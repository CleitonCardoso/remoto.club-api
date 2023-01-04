package com.remototech.remototechapi.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Email;
import com.remototech.remototechapi.services.EmailService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class EmailSenderScheduler {

	@Autowired
	private EmailService emailService;

	// 7 SEGUNDOS
	@Scheduled(fixedDelay = 7000, initialDelay = 1000)
	public void sendQueuedEmails() {
		log.info( "## Inicializando rotina de envio de emails ##" );
		List<Email> queuedEmails = emailService.listQueuedEmails();

		log.info( "## Quantidade de emails na fila: " + queuedEmails.size() );
		for (Email email : queuedEmails) {
			emailService.send( email );
		}
		log.info( "## Envio de emails finalizado ##" );
	}

}
