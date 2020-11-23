package com.remototech.remototechapi.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.PasswordRecovery;

public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, UUID> {

	List<PasswordRecovery> findAllByLoginAndActiveIsTrue(Login loginFound);

	PasswordRecovery findByRecoveryHashAndActiveIsTrue(String recoveryHash);

}
