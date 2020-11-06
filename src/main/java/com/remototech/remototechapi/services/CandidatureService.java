package com.remototech.remototechapi.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Candidature;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.repositories.CandidatureRepository;

@Service
public class CandidatureService {

	@Autowired
	private CandidatureRepository repository;
	@Autowired
	private CandidateService candidateService;

	public Candidature findByJobUuidAndCandidate(UUID jobUuid, Login loggedUser) {
		Candidate candidate = candidateService.getOrCreateIfNotExists( loggedUser );
		return repository.findByJobUuidAndCandidate( jobUuid, candidate );
	}

	public Candidature save(Candidature candidature) {
		return repository.save( candidature );
	}

}
