package com.remototech.remototechapi.controllers.priv;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.Login;

@RestController
@RequestMapping("private/user")
public class UserController extends LoggedInController {

	@GetMapping
	public Login getLoggedInfo() {
		return getLoggedUser();
	}

}
