package com.ems.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "ThisIsMyVerySecretKeyForEmployeeManagementSystemJWT2026";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24; // 24 hours


    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );
    }


    // Generate JWT Token
    public String generateToken(
            String username,
            String role) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }


    // Extract Username from Token
    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }


    // Validate JWT Token
    public boolean validateToken(
            String token,
            String username) {

        final String extractedUsername =
                extractUsername(token);

        return extractedUsername.equals(username)
                && !isTokenExpired(token);
    }


    // Check Token Expiration
    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }


    // Extract Expiration Date
    private Date extractExpiration(String token) {

        return extractAllClaims(token)
                .getExpiration();
    }


    // Extract All Claims
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}