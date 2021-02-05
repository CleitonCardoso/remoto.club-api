package com.remototech.remototechapi.services;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Email;
import com.remototech.remototechapi.entities.JobTemplateType;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.MailTemplate;
import com.remototech.remototechapi.entities.PasswordRecovery;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.LoginRepository;
import com.remototech.remototechapi.repositories.PasswordRecoveryRepository;

import freemarker.template.TemplateException;

@Service
public class PasswordRecoveryService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordRecoveryRepository recoveryRepository;

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private MailTemplateService mailTemplateService;

	@Autowired
	private TemplateProcessorService templateProcessorService;

	@Value("${frontend-url}")
	public String frontendUrl;

	@Async
	public void queueRecoveryPassword(Login loginFound) throws LoginException, GlobalException {
		if (loginFound != null) {
			disablePreviousRequestsFor( loginFound );
			createNewPasswordRecoveryFor( loginFound );
		} else {
			throw new LoginException( "Usuário ou email inválidos" );
		}
	}

	private void createNewPasswordRecoveryFor(Login login) throws GlobalException {
		try {
			PasswordRecovery passwordRecovery = buildHashAndReturnRequest( login );
			buildMailAndQueueToSend( login, passwordRecovery );
		} catch (TemplateException | IOException e) {
			throw new GlobalException( "Houve um erro na tentativa de recuperação de senha, por favor, entre em contato com contato@remoto.club", e );
		}
	}

	private void buildMailAndQueueToSend(Login login, PasswordRecovery passwordRecovery) throws TemplateException, IOException {
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

	private String buildMailContentFor(PasswordRecovery passwordRecovery) throws TemplateException, IOException {
		MailTemplate mailTemplate = mailTemplateService.findByJobTemplateType( JobTemplateType.PASSWORD_RECOVERY );

		String passwordRecoveryUrl = frontendUrl + "/new-password?recoveryHash=" + URLEncoder.encode( passwordRecovery.getRecoveryHash(), StandardCharsets.UTF_8.toString() );

		Map<String, Object> dataModel = new LinkedHashMap<>();
		dataModel.put( "login", passwordRecovery.getLogin() );
		dataModel.put( "passwordRecoveryUrl", passwordRecoveryUrl );

		String content = templateProcessorService.processTemplate( mailTemplate.getHtml(), dataModel );
		return content;
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
