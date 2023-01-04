package com.remototech.remototechapi.controllers.priv;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.AppUser;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.services.AppUserService;

@RestController
@RequestMapping("private/profile")
public class ProfileController extends LoggedInController {

	@Autowired
	private AppUserService appUserService;

	@GetMapping
	public AppUser getAppUser() {
		Login loggedUser = getLoggedUser();
		return appUserService.findByLogin( loggedUser );
	}

	@PostMapping
	public AppUser update(@Valid @RequestBody AppUser appUser) throws GlobalException {
		return appUserService.update( appUser, getLoggedUser() );
	}

}
