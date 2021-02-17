package com.remototech.remototechapi.config;

import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

	private JwtTokenUtil jwtTokenUtil;

	public HttpHandshakeInterceptor(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map attributes) throws Exception {

		if (request instanceof ServletServerHttpRequest) {

			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			String query = servletRequest.getURI().getQuery();
			Map<String, String> query_pairs = new LinkedHashMap<String, String>();
			String[] pairs = query.split( "&" );
			for (String pair : pairs) {
				int idx = pair.indexOf( "=" );
				query_pairs.put( URLDecoder.decode( pair.substring( 0, idx ), "UTF-8" ), URLDecoder.decode( pair.substring( idx + 1 ), "UTF-8" ) );
			}

			String token = query_pairs.get( "authorization" );
			String jwtToken = null;
			String username = null;
			if (token != null) {
				jwtToken = token.substring( 7 );
				// jwtToken = requestTokenHeader;
				try {
					username = jwtTokenUtil.getUsernameFromToken( jwtToken );
					if (username != null) {
						attributes.put( "username", username );
						attributes.put( "candidature", query_pairs.get( "candidature" ) );
						// attributes.put( "sessionId",
						// ((ServletServerHttpRequest)
						// request).getServletRequest().getSession().getId() );
						attributes.put( "sessionId", query_pairs.get( "candidature" ) );
						return true;
					}
				} catch (SignatureException ex) {
					log.warn( "Invalid JWT Signature" );
				} catch (MalformedJwtException ex) {
					log.warn( "Invalid JWT token" );
				} catch (ExpiredJwtException ex) {
					log.warn( "Expired JWT token" );
				} catch (UnsupportedJwtException ex) {
					log.warn( "Unsupported JWT exception" );
				} catch (IllegalArgumentException ex) {
					log.warn( "Jwt claims string is empty" );
				}
			}

		}
		return false;
	}

	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
		request.getPrincipal();
	}
}
