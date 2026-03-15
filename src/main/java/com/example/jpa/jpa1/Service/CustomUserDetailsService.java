package com.example.jpa.jpa1.Service;

import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Entity.Credential;
import com.example.jpa.jpa1.Repositoroy.CredentialRepository;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;


@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private CredentialRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			Credential credential = repository.findByUsername(username)
										.orElseThrow(()-> new UsernameNotFoundException("User not found..."));
		return new User(credential.getUsername(), credential.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + credential.getRole().name())));
	}

	
	
}
