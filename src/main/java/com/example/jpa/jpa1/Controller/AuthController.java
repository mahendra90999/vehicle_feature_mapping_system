package com.example.jpa.jpa1.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.AuthResponse;
import com.example.jpa.jpa1.Dto.CredentialDto;
import com.example.jpa.jpa1.Dto.RefreshTokenDto;
import com.example.jpa.jpa1.Service.AuthService;

@RestController
@RequestMapping("/api")
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

//    login page	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody CredentialDto credential) {
		log.info("Login attempt for username: {}", credential.getUsername());
		try {
			AuthResponse response = authService.login(credential);
            log.info("Login successful for user: {}", credential.getUsername());
            return ResponseEntity.ok(response);
		}catch (Exception e) {
			log.warn("Login failed for username: {} - Error: {}", credential.getUsername(), e.getMessage());
            throw e;
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody CredentialDto dto) {
		log.info("Signup attempt for username: {}", dto.getUsername());
		try {
            authService.signup(dto);
            log.info("User registered successfully: {}", dto.getUsername());
            return ResponseEntity.ok("User registered");
        } catch (Exception e) {
            log.error("Signup failed for username: {}", dto.getUsername(), e);
            throw e;
        }
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponseDto<String>> refreshToken(@RequestBody RefreshTokenDto request) {
		log.debug("Refresh token request received");
        try {
            ApiResponseDto<String> response = authService.refreshToken(request);
            log.debug("Token refreshed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            throw e;
        }
	}

//    for pramoting a user to admin
	@PostMapping("/admin/promote/{username}")
	public ResponseEntity<String> promoteUser(@PathVariable String username) {
		 log.warn("Admin promotion request for username: {}", username);
	        try {
	            authService.promoteUser(username);
	            log.info("User promoted to ADMIN: {}", username);
	            return ResponseEntity.ok("User promoted to ADMIN");
	        } catch (Exception e) {
	            log.error("Promotion failed for username: {}", username, e);
	            throw e;
	        }
	}
}
