package com.remototech.remototechapi.controllers.pub;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.config.JwtTokenUtil;
import com.remototech.remototechapi.config.dto.JwtRequestDto;
import com.remototech.remototechapi.config.dto.JwtResponseDto;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.services.LoginService;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private LoginService loginService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	@PostMapping
	public ResponseEntity<?> auth(@RequestBody JwtRequestDto auth) {
		Login login = loginService.findByUsername( auth.getUsername() );
		if (login == null)
			return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).build();
		return authenticate( auth.getUsername(), auth.getPassword(), login );
	}

	private ResponseEntity<?> authenticate(String username, String password, Login login) {
		try {
			authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( username, password ) );
			final String token = jwtTokenUtil.generateToken( login );
			return ResponseEntity.ok( new JwtResponseDto( token ) );
		} catch (DisabledException e) {
			return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).build();
		} catch (BadCredentialsException e) {
			return ResponseEntity.status( HttpStatus.FORBIDDEN ).build();
		}
	}

	@PostMapping("create")
	public void create(@RequestBody Login login) throws GlobalException {
		loginService.create( login );
	}

	@PostMapping("recovery")
	public void recoveryPassword(@RequestParam("email") String email) throws GlobalException {
		loginService.recoveryPassword( email );
	}
}
