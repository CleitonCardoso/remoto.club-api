package com.remototech.remototechapi.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Login;

public interface CandidateRepository extends JpaRepositoryImplementation<Candidate, UUID> {

	public Candidate findByLogin(Login login);

	@Query("SELECT candidate FROM Candidate candidate WHERE candidate.name IS NULL OR candidate.linkedInUrl IS NULL")
	@EntityGraph(value = "Candidate.login", type = EntityGraphType.LOAD)
	public List<Candidate> findAllWithIncompleteInfo();

	@Query("SELECT candidature.candidate FROM Candidature candidature"
			+ " WHERE candidature.job.uuid = :jobUuid")
	public Set<Candidate> findAllByJobUuid(@Param("jobUuid") UUID jobUuid);

}
