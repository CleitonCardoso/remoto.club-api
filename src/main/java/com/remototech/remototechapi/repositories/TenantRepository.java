package com.remototech.remototechapi.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remototech.remototechapi.entities.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, UUID> {

}
