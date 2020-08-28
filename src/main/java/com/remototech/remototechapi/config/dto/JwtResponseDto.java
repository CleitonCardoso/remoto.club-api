package com.remototech.remototechapi.config.dto;

import java.io.Serializable;

public class JwtResponseDto implements Serializable {

	private static final long serialVersionUID = 5935364075089987067L;

	private final String jwttoken;

	public JwtResponseDto(String jwttoken) {
		this.jwttoken = jwttoken;
	}

	public String getToken() {
		return this.jwttoken;
	}
}
