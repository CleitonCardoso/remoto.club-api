package com.remototech.remototechapi.services;

import java.util.List;
import java.util.UUID;

import com.remototech.remototechapi.repositories.CandidateRepository;
import com.remototech.remototechapi.repositories.LoginRepository;
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
	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private LoginRepository loginRepository;

	public Candidature findByJobUuidAndCandidate(UUID jobUuid, Login loggedUser) {
		Candidate candidate = candidateService.getOrCreateIfNotExists( loggedUser );
		return repository.findByJobUuidAndCandidate( jobUuid, candidate );
	}

	public Candidature save(Candidature candidature) {
		return repository.save( candidature );
	}

	public List <Candidature> findByCandidate(UUID loggedUserUuid ){
		UUID candidateUuid = candidateRepository.findByLogin(loginRepository.findByUuid(loggedUserUuid)).getUuid();
		return repository.findByCandidateUuid(candidateUuid);
	}
}
