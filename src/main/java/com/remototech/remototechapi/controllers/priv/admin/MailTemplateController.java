package com.remototech.remototechapi.controllers.priv.admin;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.controllers.priv.LoggedInController;
import com.remototech.remototechapi.entities.AppUser;
import com.remototech.remototechapi.entities.Email;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.MailTemplate;
import com.remototech.remototechapi.services.EmailService;
import com.remototech.remototechapi.services.MailTemplateService;
import com.remototech.remototechapi.services.TemplateProcessorService;

import freemarker.template.TemplateException;

@RestController
@RequestMapping("private/admin/mail-template")
@PreAuthorize("hasAuthority('ADMIN')")
public class MailTemplateController extends LoggedInController {

	@Autowired
	private MailTemplateService mailTemplateService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private TemplateProcessorService templateProcessor;

	@PostMapping
	public MailTemplate saveTemplate(@Valid @RequestBody MailTemplate mailTemplate) {
		return mailTemplateService.create( mailTemplate );
	}

	@GetMapping("{uuid}")
	public MailTemplate saveTemplate(@PathVariable("uuid") UUID uuid) {
		return mailTemplateService.findById( uuid );
	}

	@GetMapping
	public List<MailTemplate> findAll() {
		return mailTemplateService.findAll();
	}

	@DeleteMapping("{uuid}")
	public void delete(@PathVariable("uuid") UUID uuid) {
		mailTemplateService.delete( uuid );
	}

	@PostMapping("test")
	public void sendTestEmail(@RequestBody String template) throws TemplateException, IOException {
		Login loggedUser = getLoggedUser();
		AppUser user = loggedUser.getUser();

		user.getLogin();
		user.getName();

		String resultTemplate = templateProcessor.processTemplate( template, user );

		Email email = Email.builder()
				.sender( "contato@remoto.club" )
				.destinatary( "cleitoncardoso.dev@outlook.com" )
				.subject( "Email de teste de template - Portal.Club" )
				.text( resultTemplate )
				.build();

		emailService.send( email );
	}

}
