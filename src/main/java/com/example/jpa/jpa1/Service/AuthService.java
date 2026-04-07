package com.example.jpa.jpa1.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.AuthResponse;
import com.example.jpa.jpa1.Dto.CredentialDto;
import com.example.jpa.jpa1.Dto.RefreshTokenDto;
import com.example.jpa.jpa1.Entity.Credential;
import com.example.jpa.jpa1.Entity.RefreshToken;
import com.example.jpa.jpa1.Entity.Role;
import com.example.jpa.jpa1.Exception.DuplicateUserException;
import com.example.jpa.jpa1.Repositoroy.CredentialRepository;
import com.example.jpa.jpa1.Security.JwtUtil;

@Service
public class AuthService {

	
	private final JwtUtil jwtUtil;

	
	private final AuthenticationManager authenticationManager;

	
	private final CredentialRepository credentialRepository;

	
	private final RefreshTokenService refreshTokenService;

	
	private final PasswordEncoder passwordEncoder;

//	constroctore
	public AuthService(JwtUtil jwtUtil, AuthenticationManager authenticationManager,
			CredentialRepository credentialRepository, RefreshTokenService refreshTokenService,
			PasswordEncoder passwordEncoder) {
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
		this.credentialRepository = credentialRepository;
		this.refreshTokenService = refreshTokenService;
		this.passwordEncoder = passwordEncoder;

	}

	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	public void promoteUser(String username) {
		Credential user = credentialRepository.findByUsername(username).orElseThrow();

		user.setRole(Role.ADMIN);
		credentialRepository.save(user);
	}

	public AuthResponse login(CredentialDto credential) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword()));
		} catch (Exception e) {
			throw new RuntimeException("Invalid username or password");
		}

		Credential user = credentialRepository.findByUsername(credential.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

		return new AuthResponse(accessToken, refreshToken.getToken());

	}

	public void signup(CredentialDto dto) {
		if (credentialRepository.findByUsername(dto.getUsername()).isPresent()) {
			throw new DuplicateUserException("Username already exists");
		}

		Credential user = new Credential();
		user.setUsername(dto.getUsername());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(Role.USER);

		credentialRepository.save(user);

	}

	public ApiResponseDto<String> refreshToken(RefreshTokenDto request) {

		RefreshToken refreshToken = refreshTokenService.verifyToken(request.getRefreshToken());

		Credential user = credentialRepository.findByUsername(refreshToken.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		String newAccesToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
		log.info("Generating new access token for user: {}", refreshToken.getUsername());
		return new ApiResponseDto<>(true, "New Access token generated", newAccesToken);

	}

}
