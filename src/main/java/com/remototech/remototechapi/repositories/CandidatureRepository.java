package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;

import com.remototech.remototechapi.entities.Candidature;

public interface CandidatureRepository extends JpaRepositoryImplementation<Candidature, UUID> {

	Candidature findByJobUuidAndCandidateUuid(UUID jobUuid, UUID candidateUuid);

	@Query("Select candidature from Candidature candidature WHERE job.uuid = :jobUuid"
			+ " AND job.tenant.uuid = :tenantUuid"
			+ " AND candidate.uuid = :candidateUuid")
	Candidature findByJobUuidTenantAndCandidate(@Param(value = "jobUuid") UUID jobUuid,
			@Param("tenantUuid") UUID tenantUuid,
			@Param("candidateUuid") UUID candidateUuid);

	@Query("Select count(candidature) > 0 from Candidature candidature"
			+ " WHERE uuid = :candidatureUuid"
			+ " AND candidate.login.uuid = :loginUuid")
	boolean isCandidate(@Param("loginUuid") UUID loginUuid, @Param("candidatureUuid") UUID candidatureUuid);

	@Query("Select count(candidature) > 0 from Candidature candidature"
			+ " WHERE uuid = :candidatureUuid"
			+ " AND job.tenant.uuid = :tenantUuid")
	boolean isCreator(@Param("tenantUuid") UUID tenantUuid, @Param("candidatureUuid") UUID candidatureUuid);

}
