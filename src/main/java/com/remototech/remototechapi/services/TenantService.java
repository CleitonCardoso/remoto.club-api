package com.remototech.remototechapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.remototech.remototechapi.entities.Tenant;
import com.remototech.remototechapi.repositories.TenantRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TenantService {

	@Autowired
	private TenantRepository tenantRepository;

	public Tenant create(Tenant tenant, String partnerCode) {
		if (partnerCode != null) {
			tenant.setPartner( isValidPartnerCode( partnerCode ) );
		}

		return tenantRepository.save( tenant );
	}

	private boolean isValidPartnerCode(String partnerCode) {
		return partnerCode.equals( "PARCEIRO_POR_UM_ANO" );
	}

	public Tenant update(Tenant tenant, Tenant currentTenant) {
		tenant.setUuid( currentTenant.getUuid() );
		return tenantRepository.save( tenant );
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Tenant createOrRetrieveExistent(Tenant tenant) {
		Optional<Tenant> result = tenantRepository.findOne( Example.of( tenant ) );
		if (result.isPresent())
			return result.get();
		return create( tenant, null );
	}

}
