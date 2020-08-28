package com.remototech.remototechapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = 4772936610883721840L;

	@Value("${jwt.enable}")
	private Boolean jwtEnable;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		if (jwtEnable) {
			final String expired = (String) request.getAttribute( "expired" );
			if (expired != null)
				response.sendError( HttpServletResponse.SC_FORBIDDEN, expired );
			else
				response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
		}
	}
}
