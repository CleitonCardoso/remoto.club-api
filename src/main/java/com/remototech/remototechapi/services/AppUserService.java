package com.remototech.remototechapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remototech.remototechapi.entities.AppUser;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.AppUserRepository;

@Service
public class AppUserService {

	@Autowired
	private AppUserRepository appUserRepository;

	public AppUser findByLogin(Login login) {
		return appUserRepository.findByLogin( login );
	}

	@Transactional
	public AppUser update(AppUser appUser, Login loggedUser) throws GlobalException {
		if (!loggedUser.getUuid().equals( appUser.getLogin().getUuid() )) {
			throw new GlobalException( "Login não pertence ao usuário logado" );
		}

		boolean exists = appUserRepository.existsByUuidAndLogin( appUser.getUuid(), loggedUser );
		if (exists) {
			loggedUser.setEmail( appUser.getLogin().getEmail() );
			loggedUser.setUsername( appUser.getLogin().getUsername() );
			appUser.setLogin( loggedUser );
			return appUserRepository.save( appUser );
		} else {
			throw new GlobalException( "Perfil não pertence ao usuário logado" );
		}
	}

	public void save(AppUser appUser) {
		appUserRepository.save( appUser );
	}

}
