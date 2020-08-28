package com.remototech.remototechapi.config;

import org.springframework.security.crypto.password.PasswordEncoder;

public class DefaultPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.length() == 0)
			return false;
		if (encodedPassword.equals( rawPassword ))
			return true;

		return false;
	}

}
