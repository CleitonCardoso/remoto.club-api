package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remototech.remototechapi.entities.MailConfiguration;

public interface MailConfigurationRepository extends JpaRepository<MailConfiguration, UUID> {

	public MailConfiguration findByActiveTrue();

}
