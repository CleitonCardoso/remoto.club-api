package com.remototech.remototechapi.webclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.remototech.remototechapi.webclients.dtos.LinkedinAccessToken;

@FeignClient(name = "LinkedinOauthClient", url = "https://www.linkedin.com")
public interface LinkedinOauthClient {

	@RequestMapping(method = RequestMethod.GET, value = "/uas/oauth2/accessToken", consumes = "application/json")
	public LinkedinAccessToken getAcessToken(@RequestParam(value = "grant_type", defaultValue = "authorization_code") String authorizationCode,
			@RequestParam(value = "code") String code,
			@RequestParam(value = "client_id") String clientId,
			@RequestParam(value = "client_secret") String clientSecret,
			@RequestParam(value = "redirect_uri") String redirectUri);

}
