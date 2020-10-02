package com.remototech.remototechapi.controllers.priv;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.Sac;
import com.remototech.remototechapi.repositories.SacRepository;

@RestController
@RequestMapping("private/sac")
public class SacController {
	
	@Autowired
	private SacRepository repository;
	
	@PostMapping
	public Sac createSac(@Valid @RequestBody Sac sac) {
		System.out.println(sac);
		return repository.save(sac);
	}

}
