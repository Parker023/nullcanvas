package com.nullcanvas.blogapp.auth.jwt;

import com.nullcanvas.blogapp.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 26/05/26
 */
@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String jwtSecret;
    private SecretKey secretKey;

    @PostConstruct
    public void setSecretKey() {
        secretKey = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }


    public String generateToken(User user) {
        Date now = Date.from(Instant.now());
        Date expiry = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
        return Jwts
                .builder()
                .subject(user.getPublicId().toString())
                .claim("role", user.getRole().name())
                .claim("email", user.getEmail())
                .claim("provider", user.getProviderType().name())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {

            return false;
        }

    }

    public UUID extractUserId(String token) {

        Claims claims = extractClaims(token);

        return UUID.fromString(claims.getSubject());
    }

    public boolean isTokenValid(String token) {

        try {

            extractClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException ex) {

            return false;
        }
    }


    public Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
