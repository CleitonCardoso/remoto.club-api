package com.remototech.remototechapi.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.remototech.remototechapi.entities.Login;

public abstract class LoggedInController {

	@Transactional
	public Login getLoggedUser() {
		return (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
