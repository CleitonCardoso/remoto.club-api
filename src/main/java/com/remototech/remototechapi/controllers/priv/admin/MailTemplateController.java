package com.remototech.remototechapi.controllers.priv.admin;

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

import com.remototech.remototechapi.entities.MailTemplate;
import com.remototech.remototechapi.services.MailTemplateService;

@RestController
@RequestMapping("private/admin/mail-template")
@PreAuthorize("hasAuthority('ADMIN')")
public class MailTemplateController {

	@Autowired
	private MailTemplateService mailTemplateService;

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

}
