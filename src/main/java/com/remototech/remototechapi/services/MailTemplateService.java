package com.remototech.remototechapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.JobTemplateType;
import com.remototech.remototechapi.entities.MailTemplate;
import com.remototech.remototechapi.repositories.MailTemplateRepository;

@Service
public class MailTemplateService {

	@Autowired
	private MailTemplateRepository mailTemplateRepository;

	public MailTemplate create(MailTemplate mailTemplate) {
		return mailTemplateRepository.save( mailTemplate );
	}

	public MailTemplate findById(UUID uuid) {
		return mailTemplateRepository.findById( uuid ).orElse( null );
	}

	public List<MailTemplate> findAll() {
		return mailTemplateRepository.findAll();
	}

	public void delete(UUID uuid) {
		mailTemplateRepository.deleteById( uuid );
	}

	public MailTemplate findByJobTemplateType(JobTemplateType jobTemplateType) {
		return mailTemplateRepository.findOne( Example.of( MailTemplate.builder().jobTemplateType( jobTemplateType ).build() ) ).get();
	}

}
