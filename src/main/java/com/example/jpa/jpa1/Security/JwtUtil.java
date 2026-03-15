package com.example.jpa.jpa1.Security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final String secret = "mysecretkey15245cretkeymysecretkey12345";

	private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

	public String generateToken(String username, String role) {

		Map<String, Object> claims = new HashMap<>();
		claims.put("role", role);

		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {

		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

		return claims.getSubject();
	}

	public String generateRefreshToken(String username) {
		return UUID.randomUUID().toString(); // random token
	}

	public boolean validateToken(String token, String username) {
		String extractedUsername = extractUsername(token);
		return extractedUsername.equals(username);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

}