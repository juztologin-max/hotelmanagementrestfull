package com.hma.api.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final long SECRET_ACCESS_KEY_EXPIRATION;// in microseconds
    private final long SECRET_REFRESH_KEY_EXPIRATION;// in microseconds

    public record JwtTokenContainer(String token, long expiration) {
    }

    public long getDefaultRefreshExpiration() {
        return SECRET_REFRESH_KEY_EXPIRATION;
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

    public JwtTokenContainer generateAccessToken(String username, Set<String> role) {
        String token = Jwts.builder().subject(username).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + SECRET_ACCESS_KEY_EXPIRATION)).claim("type", "ACCESS")
                .claim("roles", role)
                .signWith(getKey()).compact();
        return new JwtTokenContainer(token, SECRET_ACCESS_KEY_EXPIRATION / 1000);

    }

    public JwtTokenContainer generateAccessTokenFromRefreshToken(String refreshToken) {

        Claims claims = extractClaims(refreshToken);
        if (!claims.get("type", String.class).equals("REFRESH")) {
            throw new SignatureException("Invalid REFRESH token");
        }
        Set<String> roles = ((List<?>) claims.get("roles")).stream().map(Object::toString).collect(Collectors.toSet());

        return generateAccessToken(claims.getSubject(), roles);
    }

    public String generateRefreshToken(String username, Set<String> roles, long expiration) {
        return Jwts.builder().subject(username).issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + SECRET_REFRESH_KEY_EXPIRATION))
                .claim("roles", roles)
                .claim("type", "REFRESH").signWith(getKey()).compact();
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
