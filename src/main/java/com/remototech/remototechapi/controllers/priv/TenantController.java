package com.remototech.remototechapi.controllers.priv;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.Tenant;
import com.remototech.remototechapi.services.TenantService;

@RestController
@RequestMapping("private/tenant")
public class TenantController extends LoggedInController {

	@Autowired
	private TenantService tenantService;

	@GetMapping
	public Tenant getTenant() {
		return getLoggedUser().getTenant();
	}

	@PostMapping
	public Tenant update(@Valid @RequestBody Tenant tenant) {
		return tenantService.update( tenant, getLoggedUser().getTenant() );
	}

}
