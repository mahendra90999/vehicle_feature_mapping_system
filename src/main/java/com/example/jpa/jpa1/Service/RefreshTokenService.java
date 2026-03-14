package com.example.jpa.jpa1.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.jpa.jpa1.Entity.RefreshToken;
import com.example.jpa.jpa1.Repositoroy.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	
	private final RefreshTokenRepository refreshTokenRepository;
	
	public RefreshToken createRefreshToken(String username) {
        RefreshToken token = RefreshToken.builder()
                .username(username)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .isRevoked(false)
                .build();	

        return refreshTokenRepository.save(token);
    }
	
	
	public RefreshToken verifyToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isRevoked() ||
                refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }

        return refreshToken;
    }

	public void revokeByUsername(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }


}
