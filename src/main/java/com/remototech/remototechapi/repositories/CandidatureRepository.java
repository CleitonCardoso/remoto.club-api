package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Candidature;

public interface CandidatureRepository extends JpaRepositoryImplementation<Candidature, UUID> {

	Candidature findByJobUuidAndCandidate(UUID jobUuid, Candidate candidate);

	@Query("Select candidature from Candidature candidature WHERE job.uuid = :jobUuid"
			+ " AND job.tenant.uuid = :tenantUuid"
			+ " AND candidate.uuid = :candidateUuid")
	Candidature findByJobUuidTenantAndCandidate(@Param(value = "jobUuid") UUID jobUuid,
			@Param(value = "tenantUuid") UUID tenantUuid,
			@Param(value = "candidateUuid") UUID candidateUuid);

}
