package com.remototech.remototechapi.scheduler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.services.IncompleteProfileNotificationService;

import freemarker.template.TemplateException;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class IncompleteProfileProcessor {

	@Autowired
	private IncompleteProfileNotificationService service;

	// 7 SEGUNDOS
	@Scheduled(cron = "0 0 0 1 1/1 *")
	public void processIncompleteProfile() throws TemplateException, IOException {
		log.info( "## Inicializando rotina de lembrete de perfis ##" );
		service.processIncompleteProfile();
		log.info( "## Lembrete de perfis finalizado ##" );
	}

}
