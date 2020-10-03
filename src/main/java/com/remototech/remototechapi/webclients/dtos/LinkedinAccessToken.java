package com.remototech.remototechapi.webclients.dtos;

import lombok.Data;

@Data
public class LinkedinAccessToken {

	private String access_token;
	private Integer expires_in;

}
