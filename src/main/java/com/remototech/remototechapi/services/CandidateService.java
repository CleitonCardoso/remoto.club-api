package com.remototech.remototechapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.repositories.CandidateRepository;

@Service
public class CandidateService {

	@Autowired
	private CandidateRepository repository;

	public Candidate getOrCreateIfNotExists(Login login) {
		Candidate candidate = repository.findByLogin( login );
		if (candidate == null) {
			candidate = Candidate.builder().login( login ).build();
			candidate = repository.save( candidate );
		}
		return candidate;
	}

	public void update(Candidate candidate) {
		repository.save( candidate );
	}

}
