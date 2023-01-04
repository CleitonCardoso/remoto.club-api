package com.remototech.remototechapi.scheduler;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.entities.Tenant;
import com.remototech.remototechapi.services.JobsService;
import com.remototech.remototechapi.services.TenantService;
import com.remototech.remototechapi.services.crawlers.ProgramathorCrawler;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ProgramathorCrawlerScheduler {

	private static final int _24_HRS = 86400000;

	@Autowired
	private ProgramathorCrawler crawler;

	@Autowired
	private JobsService jobsService;

	@Autowired
	private TenantService tenantService;

	@Scheduled(fixedDelay = _24_HRS, initialDelay = 1000)
	public void saveJobs() throws IOException {
		log.info( "## Inicializando cralwer ##" );
		List<Job> jobs = crawler.extractJobs( 2 );

		for (Job job : jobs) {
			Tenant tenant = Tenant.builder().companyName( null ).build();
			tenant.setCompanyName( job.getCompany() );
			tenant = tenantService.createOrRetrieveExistent( tenant );
			jobsService.createIfNotExists( job, tenant );
		}
		log.info( "## FINALIZANDO cralwer ##" );
	}
}
