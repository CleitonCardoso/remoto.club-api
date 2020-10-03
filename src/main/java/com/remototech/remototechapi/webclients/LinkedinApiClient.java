package com.remototech.remototechapi.webclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.remototech.remototechapi.webclients.dtos.LinkedinProfile;

@FeignClient(name = "LinkedinApiClient", url = "https://api.linkedin.com")
public interface LinkedinApiClient {

	@RequestMapping(method = RequestMethod.GET, value = "/v2/me", consumes = "application/json")
	public LinkedinProfile getUserProfile(@RequestHeader("authorization") String authorizationToken);

}
