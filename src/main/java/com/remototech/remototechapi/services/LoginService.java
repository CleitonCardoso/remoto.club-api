package com.remototech.remototechapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.AppUser;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.Role;
import com.remototech.remototechapi.entities.Tenant;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.repositories.LoginRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService implements UserDetailsService {

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private TenantService tenantService;

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LinkedInService linkedInService;

	public List<Login> findAll() {
		log.info( "{} Listando todos os Logins" );
		return this.loginRepository.findAll();
	}

	public Login findByUsername(String username) {
		log.info( "{} Listando login especifico por username" );
		return this.loginRepository.findByUsername( username );
	}

	public Login save(Login login) {
		log.info( "{} Criando Login" );
		return this.loginRepository.save( login );
	}

	public List<Login> saveOrUpdateAll(List<Login> list) {
		log.info( "{} Criando lista de Logins" );
		return this.loginRepository.saveAll( list );
	}

	public void remove(Login login) {
		log.info( "{} Removendo Login  por object" );
		this.loginRepository.delete( login );
	}

	public void deleteById(Integer id_login) {
		log.info( "{} Removendo Logins por id " + id_login );
		this.loginRepository.deleteById( id_login );
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return (UserDetails) findByUsername( username );
	}

	public void create(Login login, String partnerCode) throws GlobalException {
		validateLogin( login );
		login.setPassword( passwordEncoder.encode( login.getPassword() ) );
		if (login.getRole() == Role.COMPANY) {
			Tenant tenant = tenantService.create( login.getTenant(), partnerCode );
			login.setTenant( tenant );
		}
		Login loginSaved = loginRepository.save( login );

		AppUser appUser = AppUser.builder()
				.name( login.getUsername() )
				.login( loginSaved )
				.build();

		if (login.getTenant() != null) {
			appUser.setTenant( login.getTenant() );
		}

		appUserService.save( appUser );
	}

	private void validateLogin(Login login) throws GlobalException {
		String username = login.getUsername();
		boolean existsByUsername = loginRepository.existsByUsername( username );

		if (existsByUsername) {
			throw new GlobalException( "Já existe um usuário com este login cadastrado!" );
		}

		boolean existsByEmail = loginRepository.existsByEmail( login.getEmail() );

		if (existsByEmail) {
			throw new GlobalException( "Já existe um usuário com este email cadastrado!" );
		}

	}

	public void recoveryPassword(String email) throws GlobalException {
		Login loginFound = loginRepository.findByEmail( email );
		if (loginFound == null) {
			throw new GlobalException( "Email não cadastrado no sistema." );
		}

	}

	public Login findByLinkedInId(String linkedInId) {
		return loginRepository.findByLinkedInId( linkedInId );
	}

	public void createSocial(Login login, String linkedInCode, String redirectUri, String partnerCode) throws GlobalException {
		validateLogin( login );

		String linkedInId = linkedInService.getLinkedInId( linkedInCode, redirectUri );

		validateLinkedinId( linkedInId );

		login.setLinkedInId( linkedInId );
		login.setRole( Role.COMPANY );
		Tenant tenant = tenantService.create( login.getTenant(), partnerCode );
		login.setTenant( tenant );

		Login loginSaved = loginRepository.save( login );

		AppUser appUser = AppUser.builder().name( login.getUsername() ).login( loginSaved ).tenant( tenant ).build();
		appUserService.save( appUser );
	}

	private void validateLinkedinId(String linkedInId) throws GlobalException {
		if (loginRepository.existsByLinkedInId( linkedInId )) {
			throw new GlobalException( "Usuário já cadastrado com o LinkedIn" );
		}
	}

}
