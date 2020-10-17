package com.remototech.remototechapi.controllers.priv;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.controllers.LoggedInController;
import com.remototech.remototechapi.entities.Sac;
import com.remototech.remototechapi.services.SacService;

@RestController
@RequestMapping("private/sac")
public class SacController extends LoggedInController {
	
	@Autowired
	private SacService service;
	
	@PostMapping
	public Sac update(@Valid @RequestBody Sac sac) {
		return service.save( sac, getLoggedUser() );
	}

}
