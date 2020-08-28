package com.remototech.remototechapi.config.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class JwtRequestDto implements Serializable {

	private static final long serialVersionUID = 5271437043597069374L;

	private String username;

	private String password;

	public JwtRequestDto() {
	}

	public JwtRequestDto(String username, String password) {

		this.setUsername( username );
		this.setPassword( password );
	}
}
