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
import com.example.jpa.jpa1.Repository.CredentialRepository;
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
		log.info("Attempting to promote user to ADMIN: {}", username);
		try {
			Credential user = credentialRepository.findByUsername(username).orElseThrow(() -> {
				log.error("User not found for promotion: {}", username);
				return new RuntimeException("User not found");
			});

			user.setRole(Role.ADMIN);
			credentialRepository.save(user);
			log.info("User successfully promoted to ADMIN: {}", username);
		} catch (Exception e) {
			log.error("Failed to promote user {}: {}", username, e.getMessage());
			throw e;
		}
	}

	public AuthResponse login(CredentialDto credential) {
		log.info("Login attempt for username: {}", credential.getUsername());
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword()));

			log.debug("Authentication successful for user: {}", credential.getUsername());
		} catch (Exception e) {
			log.warn("Authentication failed for username: {}", credential.getUsername());
			throw new RuntimeException("Invalid username or password");
		}

		Credential user = credentialRepository.findByUsername(credential.getUsername()).orElseThrow(() -> {
			log.error("User not found after authentication: {}", credential.getUsername());
			return new RuntimeException("User not found");
		});

		String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
		log.info("Access token generated for user: {}", user.getUsername());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());
		log.info("Refresh token created for user: {}", user.getUsername());

		return new AuthResponse(accessToken, refreshToken.getToken());

	}

	public void signup(CredentialDto dto) {
		log.info("Signup attempt for username: {}", dto.getUsername());

		if (credentialRepository.findByUsername(dto.getUsername()).isPresent()) {
			log.warn("Signup failed - Username already exists: {}", dto.getUsername());
			throw new DuplicateUserException("Username already exists");
		}

		Credential user = new Credential();
		user.setUsername(dto.getUsername());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setRole(Role.USER);

		credentialRepository.save(user);
		log.info("User registered successfully with username: {}", dto.getUsername());

	}

	public ApiResponseDto<String> refreshToken(RefreshTokenDto request) {

		log.debug("Processing refresh token request");
		try {
			RefreshToken refreshToken = refreshTokenService.verifyToken(request.getRefreshToken());
			log.debug("Refresh token verified for user: {}", refreshToken.getUsername());

			Credential user = credentialRepository.findByUsername(refreshToken.getUsername()).orElseThrow(() -> {
				log.error("User not found for token refresh: {}", refreshToken.getUsername());
				return new RuntimeException("User not found");
			});

			String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
			log.info("New access token generated for user: {}", refreshToken.getUsername());
			return new ApiResponseDto<>(true, "New Access token generated", newAccessToken);
		} catch (Exception e) {
			log.error("Token refresh failed: {}", e.getMessage());
			throw e;
		}

	}

}
