package com.example.jpa.jpa1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.CredentialDto;
import com.example.jpa.jpa1.Entity.Credential;
import com.example.jpa.jpa1.Repositoroy.CredentialRepository;

@Service
public class SignupService {
	
	@Autowired
	private CredentialRepository credentialRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public CredentialDto saveuser(CredentialDto credentialDto) {
		Credential credential = new Credential();
        credential.setUsername(credentialDto.getUsername());
        credential.setPassword(passwordEncoder.encode(credentialDto.getPassword()));

        
        Credential saved = credentialRepository.save(credential);
        CredentialDto result = new CredentialDto();
        result.setUsername(saved.getUsername());
        result.setPassword(saved.getPassword());
        
        
        return result;
	}
	
}
