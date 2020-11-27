package com.remototech.remototechapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.entities.Tenant;

public interface JobsRepository extends JpaRepositoryImplementation<Job, UUID> {

	public Job findByUuidAndTenantOrderByJobStatusDesc(UUID uuid, Tenant tenant);

	public void removeByUuidAndTenant(UUID uuid, Tenant tenant);

	public boolean existsByUuidAndCandidatesIn(UUID jobUuid, List<Candidate> candidates);

	public Job findByUuidOrderByJobStatusDesc(UUID uuid);


}
