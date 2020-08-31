package com.remototech.remototechapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Tenant;
import com.remototech.remototechapi.repositories.TenantRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantService {

	@Autowired
	private TenantRepository tenantRepository;

	public Tenant create(Tenant tenant) {
		return tenantRepository.save( tenant );
	}

	public Tenant update(Tenant tenant, Tenant currentTenant) {
		System.out.println( currentTenant );
		System.out.println( tenant );
		tenant.setUuid( currentTenant.getUuid() );
		return tenantRepository.save( tenant );
	}

}
