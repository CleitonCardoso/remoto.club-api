package com.remototech.remototechapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Candidature;

public interface CandidatureRepository extends JpaRepositoryImplementation<Candidature, UUID> {

	Candidature findByJobUuidAndCandidate(UUID jobUuid, Candidate candidate);

	List<Candidature> findByCandidateUuid(UUID uuidCandidate);

}
