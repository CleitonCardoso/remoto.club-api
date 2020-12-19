package com.remototech.remototechapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.remototech.remototechapi.entities.AppUser;
import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.Role;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.AppUserRepository;

@Service
public class AppUserService {

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private CandidateService candidateService;

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
			Login loginEdited = appUser.getLogin();

			loggedUser.setEmail( loginEdited.getEmail() );
			loggedUser.setUsername( loginEdited.getUsername() );
			if (Role.CANDIDATE.equals( loggedUser.getRole() ) && loggedUser.getCandidate() != null) {
				loggedUser.getCandidate().setName( appUser.getName() );
				loggedUser.getCandidate().setLinkedInUrl( loginEdited.getCandidate().getLinkedInUrl() );
				candidateService.update( loggedUser.getCandidate() );
			}

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
