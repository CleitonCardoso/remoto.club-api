package com.remototech.remototechapi.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.services.JobsService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CloseOldJobs {

	@Autowired
	private JobsService jobsService;

	// Every day 18 
	@Scheduled(cron = "* * * 18 * *")
	public void closeOldJobs() {
		log.info( "## Inicializando rotina fechamento de vagas antigas ##" );
		jobsService.closeOldJobs();
		log.info( "## Rotina fechamento de vagas antigas ##" );
	}

}
