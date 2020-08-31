package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.remototech.remototechapi.entities.AppUser;
import com.remototech.remototechapi.entities.Login;

public interface AppUserRepository extends JpaRepositoryImplementation<AppUser, UUID> {

	AppUser findByLogin(Login login);

	boolean existsByUuidAndLogin(UUID uuid, Login login);
}
