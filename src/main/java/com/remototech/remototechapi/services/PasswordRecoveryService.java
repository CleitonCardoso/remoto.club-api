package com.remototech.remototechapi.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Email;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.PasswordRecovery;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.LoginRepository;
import com.remototech.remototechapi.repositories.PasswordRecoveryRepository;

import org.springframework.beans.factory.annotation.Value;

@Service
public class PasswordRecoveryService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordRecoveryRepository recoveryRepository;

	@Autowired
	private LoginRepository loginRepository;

	@Value("${frontend-url}")
	public String frontendUrl;

	@Async
	public void queueRecoveryPassword(Login loginFound) throws LoginException {
		if (loginFound != null) {
			disablePreviousRequestsFor( loginFound );
			createNewPasswordRecoveryFor( loginFound );
		} else {
			throw new LoginException( "Usuário ou email inválidos" );
		}
	}

	private void createNewPasswordRecoveryFor(Login login) {
		PasswordRecovery passwordRecovery = buildHashAndReturnRequest( login );
		buildMailAndQueueToSend( login, passwordRecovery );
	}

	private void buildMailAndQueueToSend(Login login, PasswordRecovery passwordRecovery) {
		Email email = Email.builder()
				.sender( "contato@remoto.club" )
				.destinatary( login.getEmail() )
				.subject( "Recuperação de senha - Remoto.Club" )
				.text( buildMailContentFor( passwordRecovery ) )
				.build();
		emailService.queueToSend( email );

		passwordRecovery.setEmail( email );
		recoveryRepository.save( passwordRecovery );
	}

	private String buildMailContentFor(PasswordRecovery passwordRecovery) {
		String username = passwordRecovery.getLogin().getUsername();

		StringBuilder emailContent = new StringBuilder( "Olá " + username + ", " );
		emailContent.append( "<br>"
				+ "<br>" );
		emailContent.append( "Você está recebendo este email porque foi solicitado uma recuperação de senha no <strong>Remoto.Club</strong>. Caso você não tenha conhecimento desta ação, ignore." );
		emailContent.append( "<br>"
				+ "<br>"
				+ "<br>" );
		emailContent.append( "Clique no link para recuperar sua senha: " );
		try {
			emailContent.append( "<a href=\"" + frontendUrl + "/new-password?recoveryHash=" + URLEncoder.encode( passwordRecovery.getRecoveryHash(), StandardCharsets.UTF_8.toString() ) + "\"> Recuperar Senha </a>" );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return emailContent.toString();
	}

	private PasswordRecovery buildHashAndReturnRequest(Login login) {
		PasswordRecovery passwordRecovery = PasswordRecovery.builder()
				.active( true )
				.login( login )
				.build();

		String recoveryHash = generateHashFor( new Date(), login.getUuid(), login.getEmail() );
		passwordRecovery.setRecoveryHash( recoveryHash );
		return recoveryRepository.save( passwordRecovery );
	}

	private String generateHashFor(Date requestedDate, UUID uuid, String email) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		List<String> hashFields = new ArrayList<>();
		hashFields.add( requestedDate.toString() );
		hashFields.add( uuid.toString() );
		hashFields.add( email );

		Collections.shuffle( hashFields );

		StringBuilder hashBuilder = new StringBuilder();

		for (String field : hashFields) {
			hashBuilder.append( field );
			hashBuilder.append( "-" );
		}

		return encoder.encode( hashBuilder.toString() );
	}

	private void disablePreviousRequestsFor(Login loginFound) {
		List<PasswordRecovery> previousRequests = recoveryRepository.findAllByLoginAndActiveIsTrue( loginFound );
		for (PasswordRecovery passwordRecovery : previousRequests) {
			passwordRecovery.setActive( false );
			recoveryRepository.save( passwordRecovery );
		}
	}

	public void recoveryPassword(String recoveryHash, String newPasswordEncoded) throws GlobalException {
		PasswordRecovery passwordRecovery = recoveryRepository.findByRecoveryHashAndActiveIsTrue( recoveryHash );
		if (passwordRecovery != null) {
			Login loginFound = passwordRecovery.getLogin();
			loginFound.setPassword( newPasswordEncoded );
			loginRepository.save( loginFound );
			passwordRecovery.setActive( false );
			recoveryRepository.save( passwordRecovery );
		} else {
			throw new GlobalException( "Nenhuma solicitação de recuperação de senha encontrada" );
		}

	}

}
