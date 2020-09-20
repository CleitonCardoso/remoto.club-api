package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.remototech.remototechapi.entities.Candidate;

public interface CandidateRepository extends JpaRepositoryImplementation<Candidate, UUID> {

	public Candidate findByLinkedInUrl(String linkedInUrl);

}
