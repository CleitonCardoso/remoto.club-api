package com.remototech.remototechapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remototech.remototechapi.entities.Email;
import com.remototech.remototechapi.entities.EmailStatus;

public interface EmailRepository extends JpaRepository<Email, UUID> {

	public List<Email> findAllByStatus(EmailStatus status);

}
