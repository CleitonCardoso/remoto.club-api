package com.remototech.remototechapi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.Tenant;
import com.remototech.remototechapi.repositories.LoginRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService implements UserDetailsService {

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private TenantService tenantService;

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

	public void create(Login login) {
		Tenant tenant = tenantService.create( login.getTenant() );
		login.setTenant( tenant );
		loginRepository.save( login );
	}

}
