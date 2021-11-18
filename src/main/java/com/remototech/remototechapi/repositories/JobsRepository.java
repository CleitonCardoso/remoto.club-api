package com.remototech.remototechapi.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.entities.Tenant;

public interface JobsRepository extends JpaRepositoryImplementation<Job, UUID> {

	public Job findByUuidAndTenantOrderByJobStatusDesc(UUID uuid, Tenant tenant);

	public void removeByUuidAndTenant(UUID uuid, Tenant tenant);

	public boolean existsByUuidAndCandidatesIn(UUID jobUuid, List<Candidate> candidates);

	public Job findByUuidOrderByJobStatusDesc(UUID uuid);

	@Transactional
	@Modifying
	@Query("Update Job job set job.jobStatus = 'CLOSED' where "
			+ "(lastUpdate is null AND createdDate <= :date) "
			+ "OR "
			+ "(lastUpdate <= :date)")
	public void closeOlderThanFourMonths(@Param("date") LocalDateTime date);

}
