package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.entities.Tenant;

public interface JobsRepository extends JpaRepositoryImplementation<Job, UUID> {

	public Job findByUuidAndTenant(UUID uuid, Tenant tenant);

}
