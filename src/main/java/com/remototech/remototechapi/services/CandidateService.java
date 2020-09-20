package com.remototech.remototechapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.repositories.CandidateRepository;

@Service
public class CandidateService {

	@Autowired
	private CandidateRepository repository;

	public Candidate getOrCreateIfNotExists(String linkedInUrl) {
		Candidate candidate = repository.findByLinkedInUrl( linkedInUrl );
		if (candidate == null) {
			candidate = Candidate.builder().linkedInUrl( linkedInUrl ).build();
			candidate = repository.save( candidate );
		}
		return candidate;
	}

	public void update(Candidate candidate) {
		repository.save( candidate );
	}

}
