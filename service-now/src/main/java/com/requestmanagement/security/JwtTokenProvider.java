package com.requestmanagement.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret:}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs:604800000}")
    private int jwtExpirationInMs;

    private SecretKey getSigningKey() {
        // If no secret is configured, generate a secure one
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            logger.warn("No JWT secret configured. Generating a secure key. This should be configured in production!");
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }

        // If the secret is Base64 encoded, decode it
        try {
            byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
            if (decodedKey.length >= 32) { // 256 bits
                return Keys.hmacShaKeyFor(decodedKey);
            }
        } catch (IllegalArgumentException e) {
            // Not Base64, continue to use as plain string
        }

        // If plain string, ensure it's long enough
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "JWT secret must be at least 256 bits (32 bytes). Current length: " +
                            (keyBytes.length * 8) + " bits. Use Keys.secretKeyFor() or provide a longer secret."
            );
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SecurityException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty");
        }
        return false;
    }
}