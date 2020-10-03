package com.remototech.remototechapi.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.Sac;
import com.remototech.remototechapi.repositories.SacRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SacService {

	@Autowired
	private SacRepository repository;
	
	@Transactional 
	public Sac save(Sac sac, Login login) {		
		sac.setLogin(login);
		return repository.save( sac );
	}

}
