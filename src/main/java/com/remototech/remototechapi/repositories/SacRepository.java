package com.remototech.remototechapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.remototech.remototechapi.entities.Sac;

@Repository
public interface SacRepository extends JpaRepository<Sac, Long>{

}