package com.remototech.remototechapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.webclients.LinkedinApiClient;
import com.remototech.remototechapi.webclients.LinkedinOauthClient;
import com.remototech.remototechapi.webclients.dtos.LinkedinAccessToken;
import com.remototech.remototechapi.webclients.dtos.LinkedinProfile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LinkedInService {

	private static final String GRANT_TYPE = "authorization_code";
	private static final String CLIENT_ID = "77sps93aqlw7mu";
	private static final String CLIENT_SECRET = "Ez6PdJRot0t5nue8";

	@Autowired
	private LinkedinApiClient linkedinApiClient;

	@Autowired
	private LinkedinOauthClient linkedinOauthClient;

	public String getLinkedInId(String linkedInCode, String redirectUri) throws GlobalException {
		try {
			LinkedinAccessToken accessToken = linkedinOauthClient.getAcessToken( GRANT_TYPE, linkedInCode, CLIENT_ID, CLIENT_SECRET, redirectUri );
			LinkedinProfile userProfile = linkedinApiClient.getUserProfile( "Bearer " + accessToken.getAccess_token() );
			return userProfile.getId();
		} catch (Exception e) {
			log.error( "Login with facebook could not be acquired", e );
			throw new GlobalException( "Não foi possível fazer login com o LinkedIn." );
		}
	}

}
