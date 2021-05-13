package com.remototech.remototechapi.services.crawlers;

import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.CrawlerExecutionLog;

@Service
public class CrawlerService {

	public void logCrawlerExecution(String crawlerName, int numberOfJobs, String status) {
		CrawlerExecutionLog.builder()
				.crawlerName( crawlerName )
				.numberOfJobsRetrieved( numberOfJobs )
				.executionStatus( status )
				.build();
	}

}
