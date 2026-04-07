package com.example.jpa.jpa1.Controller;

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

	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

//    login page	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody CredentialDto credential) {
		return ResponseEntity.ok(authService.login(credential));
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody CredentialDto dto) {
		authService.signup(dto);
		return ResponseEntity.ok("User registered");
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponseDto<String>> refreshToken(@RequestBody RefreshTokenDto request) {

		return ResponseEntity.ok(authService.refreshToken(request));
	}

//    for pramoting a user to admin
	@PostMapping("/admin/promote/{username}")
	public ResponseEntity<String> promoteUser(@PathVariable String username) {
		authService.promoteUser(username);
		return ResponseEntity.ok("User promoted to ADMIN");
	}
}
