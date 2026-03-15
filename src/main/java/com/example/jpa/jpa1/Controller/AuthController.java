package com.example.jpa.jpa1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.jpa1.Dto.ApiResponseDto;
import com.example.jpa.jpa1.Dto.AuthResponse;
import com.example.jpa.jpa1.Dto.CredentialDto;
import com.example.jpa.jpa1.Dto.RefreshTokenDto;
import com.example.jpa.jpa1.Entity.Credential;
import com.example.jpa.jpa1.Entity.RefreshToken;
import com.example.jpa.jpa1.Entity.Role;
import com.example.jpa.jpa1.Repositoroy.CredentialRepository;
import com.example.jpa.jpa1.Security.JwtUtil;
import com.example.jpa.jpa1.Service.AuthService;
import com.example.jpa.jpa1.Service.RefreshTokenService;

@RestController
@RequestMapping("/api")
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CredentialRepository credentialRepository;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private AuthService authService;

//    login page
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody Credential credential) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}

		Credential user = credentialRepository.findByUsername(credential.getUsername()).orElseThrow();

//    	// ROLE VALIDATION
//        if (!user.getRole().equals(credential.getRole())) {
//            return ResponseEntity.status(403).body(new AuthResponse(null, "You cannot login with this role"));
//        }

		String accesstoken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(credential.getUsername());
		String refrestokenstring = refreshToken.getToken();
		AuthResponse response = new AuthResponse(accesstoken, refrestokenstring);

		return ResponseEntity.ok(response);
	}

//    for signup
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody CredentialDto dto) {
		Credential user = new Credential();
		user.setUsername(dto.getUsername());
		user.setPassword(passwordEncoder.encode(dto.getPassword())); // <-- encode here
		user.setRole(Role.USER);
		credentialRepository.save(user);
		return ResponseEntity.ok("User registered");
	}

//    to generate new access token
	@PostMapping("/refresh-token")
	public ApiResponseDto<String> refreshToken(@RequestBody RefreshTokenDto request) {

		RefreshToken refreshToken = refreshTokenService.verifyToken(request.getRefreshToken());

		Credential user = credentialRepository.findByUsername(refreshToken.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

		return new ApiResponseDto<>(true, "New access token generated", newAccessToken);
	}

//    for pramoting a user to admin
	@PostMapping("/admin/promote/{username}")
	public ResponseEntity<String> promoteUser(@PathVariable String username) {
		authService.promoteUser(username);
		return ResponseEntity.ok("User promoted to ADMIN");
	}
}
