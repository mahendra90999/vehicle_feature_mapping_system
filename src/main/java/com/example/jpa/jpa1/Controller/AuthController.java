package com.example.jpa.jpa1.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.jpa.jpa1.Repositoroy.CredentialRepository;
import com.example.jpa.jpa1.Security.JwtUtil;
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
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Credential credential) {
    	try {
    		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credential.getUsername(),credential.getPassword()));			
		} catch (Exception e) {
			 return ResponseEntity.badRequest().build();
		}
    	
    	String accesstoken = jwtUtil.generateToken(credential.getUsername());
    	
    	RefreshToken refreshToken = refreshTokenService.createRefreshToken(credential.getUsername());
    	String refrestokenstring = refreshToken.getToken();
    	AuthResponse response = new AuthResponse(accesstoken, refrestokenstring);

    	
    	return ResponseEntity.ok(response);
    }
    
    
    
    
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody CredentialDto dto) {
        Credential user = new Credential();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));  // <-- encode here
        credentialRepository.save(user);
        return ResponseEntity.ok("User registered");
    }
    
    
    @PostMapping("/refresh-token")
    public ApiResponseDto<String> refreshToken(@RequestBody RefreshTokenDto  request) {

        RefreshToken refreshToken = refreshTokenService.verifyToken(request.getRefreshToken());

        String newAccessToken = jwtUtil.generateToken(refreshToken.getUsername());

        return new ApiResponseDto<>(true, "New access token generated", newAccessToken);
    }

}
