package com.hma.api;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {
	private final String SECRET_KEY;
	private final long SECRET_ACCESS_KEY_EXPIRATION;
	private final long SECRET_REFRESH_KEY_EXPIRATION;

	public record JwtTokenContainer(String token, long expiration) {
	}

	public JwtService(@Value("${JWT_SECRET_KEY}") String SECRET_KEY,
			@Value("${JWT_SECRET_ACCESS_KEY_EXPIRATION}") long SECRET_ACCESS_KEY_EXPIRATION,
			@Value("${JWT_SECRET_REFRESH_KEY_EXPIRATION}") long SECRET_REFRESH_KEY_EXPIRATION) {
		this.SECRET_KEY = SECRET_KEY;
		this.SECRET_ACCESS_KEY_EXPIRATION = SECRET_ACCESS_KEY_EXPIRATION;
		this.SECRET_REFRESH_KEY_EXPIRATION = SECRET_REFRESH_KEY_EXPIRATION;
	}

	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}

	public JwtTokenContainer generateAccessToken(String username) {
		String token = Jwts.builder().subject(username).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + SECRET_ACCESS_KEY_EXPIRATION)).claim("type", "ACCESS")
				.signWith(getKey()).compact();
		return new JwtTokenContainer(token, SECRET_ACCESS_KEY_EXPIRATION / 1000);

	}

	public JwtTokenContainer generateRefreshToken(String username) {
		String token = Jwts.builder().subject(username).issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + SECRET_REFRESH_KEY_EXPIRATION))
				.claim("type", "REFRESH").signWith(getKey()).compact();
		return new JwtTokenContainer(token, SECRET_REFRESH_KEY_EXPIRATION / 1000);

	}

	public JwtTokenContainer generateAccessTokenFromRefreshToken(String refreshToken) {
		Claims claims = extractClaims(refreshToken);
		if (!claims.get("type", String.class).equals("REFRESH")) {
			throw new SignatureException("Invalid REFRESH token");
		}

		return generateAccessToken(claims.getSubject());
	}

	public Claims extractClaims(String jws) {
		return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(jws).getPayload();
	}

	public String extractUsername(String jws) {
		return extractClaims(jws).getSubject();
	}

	public boolean isExpired(String jws) {
		return extractClaims(jws).getExpiration().before(new Date());
	}

}
