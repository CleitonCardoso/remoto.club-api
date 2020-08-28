package com.remototech.remototechapi.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.remototech.remototechapi.services.LoginService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${jwt.enable}")
	private Boolean jwtEnable;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private LoginService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService( jwtUserDetailsService ).passwordEncoder( passwordEncoder() );
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new DefaultPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods( Arrays.asList( "POST", "GET", "PUT", "DELETE", "OPTIONS" ) );
		configuration.setAllowCredentials( true );
		configuration.addAllowedOrigin( "*" );
		configuration.addAllowedHeader( "*" );
		configuration.setExposedHeaders( Arrays.asList( HttpHeaders.CONTENT_DISPOSITION ) );
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration( "/**", configuration );
		return source;
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors();
		if (jwtEnable) {
			// We don't need CSRF for this example
			httpSecurity.csrf().disable()
					// dont authenticate this particular request
					.authorizeRequests().antMatchers( "/v2/api-docs",
							"/configuration/ui",
							"/swagger-resources/**",
							"/configuration/security",
							"/swagger-ui.html",
							"/webjars/**",
							"/login",
							"/login/*",
							"/public/*" )
					.permitAll()
					.antMatchers( HttpMethod.OPTIONS, "/**" )
					.permitAll().
					// all other requests need to be authenticated
					anyRequest().authenticated().and().
					// make sure we use stateless session; session won't be used
					// to
					// store user's state.
					exceptionHandling().authenticationEntryPoint( jwtAuthenticationEntryPoint ).and()
					.sessionManagement()
					.sessionCreationPolicy( SessionCreationPolicy.STATELESS );
			// Add a filter to validate the tokens with every request
			httpSecurity.addFilterBefore( jwtRequestFilter, UsernamePasswordAuthenticationFilter.class );
		}
	}
}
