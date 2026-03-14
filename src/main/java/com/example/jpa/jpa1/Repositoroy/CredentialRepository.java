package com.example.jpa.jpa1.Repositoroy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jpa.jpa1.Entity.Credential;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
	
	Optional<Credential> findByUsername(String username);
	

}
