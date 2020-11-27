package com.remototech.remototechapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.MailConfiguration;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.MailConfigurationRepository;

@Service
public class MailConfigurationService {

	@Autowired
	private MailConfigurationRepository mailConfigurationRepository;

	@Autowired
	private RestartEndpoint restartEndpoint;

	public MailConfiguration getMailConfiguration() {
		return mailConfigurationRepository.findByActiveTrue();
	}

	public MailConfiguration saveMailConfiguration(MailConfiguration mailConfiguration) throws GlobalException {
		Exception error = null;
		try {
			mailConfiguration.setActive( true );
			return mailConfigurationRepository.save( mailConfiguration );
		} catch (Exception e) {
			error = e;
			throw new GlobalException( e.getMessage() );
		} finally {
			if (error == null)
				restartEndpoint.restart();
		}
	}

}
