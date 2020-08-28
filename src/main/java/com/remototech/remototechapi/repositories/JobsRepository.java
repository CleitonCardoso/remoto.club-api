package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.remototech.remototechapi.entities.Job;

public interface JobsRepository extends JpaRepositoryImplementation<Job, UUID> {

}
