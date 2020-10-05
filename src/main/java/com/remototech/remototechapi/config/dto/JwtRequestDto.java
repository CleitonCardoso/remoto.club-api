package com.remototech.remototechapi.config.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestDto implements Serializable {

	private static final long serialVersionUID = 5271437043597069374L;

	private String username;

	private String password;

	private String linkedInCode;

	private String redirectUri;

}
