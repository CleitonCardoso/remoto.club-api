package com.remototech.remototechapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.remototech.remototechapi.entities.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {

	public Login findByUsername(String username);

	public boolean existsByUsername(String username);

	public boolean existsByEmail(String email);

	public Login findByEmail(String email);

	public Login findByLinkedInId(String linkedInCode);

	public boolean existsByLinkedInId(String linkedInId);

}
