package com.remototech.remototechapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.remototech.remototechapi.entities.Login;

@Repository
public interface LoginRepository extends JpaRepository<Login, Integer> {

	public Login findByUsername(String username);

}
