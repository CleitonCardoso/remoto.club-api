package com.remototech.remototechapi.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.services.LoginService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private LoginService loginService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.enable}")
	private Boolean jwtEnable;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		if (jwtEnable == null || jwtEnable.booleanValue() == false) {
			chain.doFilter( request, response );
			return;
		}
		logger.warn( request.getHeader( "Authorization" ) );
		final String requestTokenHeader = request.getHeader( "Authorization" );

		String username = null;

		String jwtToken = null;

		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		// if (requestTokenHeader != null &&
		// requestTokenHeader.startsWith("Bearer ")) {
		if (requestTokenHeader != null) {
			jwtToken = requestTokenHeader.substring( 7 );
			// jwtToken = requestTokenHeader;
			try {
				username = jwtTokenUtil.getUsernameFromToken( jwtToken );
			} catch (SignatureException ex) {
				log.warn( "Invalid JWT Signature" );
			} catch (MalformedJwtException ex) {
				log.warn( "Invalid JWT token" );
			} catch (ExpiredJwtException ex) {
				log.warn( "Expired JWT token" );
				request.setAttribute( "expired", ex.getMessage() );
			} catch (UnsupportedJwtException ex) {
				log.warn( "Unsupported JWT exception" );
			} catch (IllegalArgumentException ex) {
				log.warn( "Jwt claims string is empty" );
			}
		} else
			logger.warn( "JWT Token does not begin with Bearer String" );
		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			Login user = this.loginService.findByUsername( username );
			// if token is valid configure Spring Security to manually set
			// authentication
			if (jwtTokenUtil.validateToken( jwtToken, user )) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						user, null, user.getAuthorities() );
				usernamePasswordAuthenticationToken
						.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication( usernamePasswordAuthenticationToken );
			}
		}
		chain.doFilter( request, response );
	}
}
