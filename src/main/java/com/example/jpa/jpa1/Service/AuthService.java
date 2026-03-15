package com.example.jpa.jpa1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Entity.Credential;
import com.example.jpa.jpa1.Entity.Role;
import com.example.jpa.jpa1.Repositoroy.CredentialRepository;

@Service
public class AuthService {

	@Autowired
	private CredentialRepository credentialRepository;

	public void promoteUser(String username) {
		Credential user = credentialRepository.findByUsername(username).orElseThrow();

		user.setRole(Role.ADMIN);
		credentialRepository.save(user);
	}

}
