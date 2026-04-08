package com.example.jpa.jpa1.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.CredentialDto;
import com.example.jpa.jpa1.Repository.CredentialRepository;

import jakarta.transaction.Transactional;

@Service
public class loginService {
	
	@Autowired
	private CredentialRepository credentialRepository;
	
	
	@Transactional
	public boolean login(CredentialDto credentialDto) {
			
		return credentialRepository.findByUsername(credentialDto.getUsername() ).isPresent();
	
	}
	
	
}
