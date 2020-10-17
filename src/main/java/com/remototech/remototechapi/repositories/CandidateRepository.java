package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Login;

public interface CandidateRepository extends JpaRepositoryImplementation<Candidate, UUID> {

	public Candidate findByLogin(Login login);

}
