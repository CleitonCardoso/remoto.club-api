package com.remototech.remototechapi.controllers.priv.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.MailConfiguration;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.services.MailConfigurationService;

@RestController
@RequestMapping("private/admin/mail-config")
public class MailConfigurationController {

	@Autowired
	private MailConfigurationService mailConfigurationService;

	@GetMapping
	public MailConfiguration getMailConfiguration() {
		return mailConfigurationService.getMailConfiguration();
	}

	@PostMapping
	public MailConfiguration saveMailConfiguration(@RequestBody MailConfiguration mailConfiguration) throws GlobalException {
		return mailConfigurationService.saveMailConfiguration( mailConfiguration );
	}
}
