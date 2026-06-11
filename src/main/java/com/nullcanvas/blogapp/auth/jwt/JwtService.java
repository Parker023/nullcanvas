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
 * Issues and validates the HMAC-signed JWTs used as access tokens.
 *
 * <p>
 * Tokens encode the user's public id as the subject plus role, email and provider claims,
 * and are signed with a symmetric key derived from the {@code JWT_SECRET} property.
 * Validation re-verifies the signature and expiry on every request.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
@Service
public class JwtService {

	/**
	 * Raw signing secret injected from configuration; used to derive {@link #secretKey}.
	 */
	@Value("${JWT_SECRET}")
	private String jwtSecret;

	/**
	 * HMAC signing key derived once from {@link #jwtSecret} and reused for sign/verify.
	 */
	private SecretKey secretKey;

	/**
	 * Derives the HMAC key from the configured secret once the bean is fully initialised.
	 */
	@PostConstruct
	public void setSecretKey() {
		// Build the HMAC-SHA key from the secret's UTF-8 bytes; done once to avoid
		// re-deriving per call.
		secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Builds a signed JWT for the given user, valid for one hour.
	 * @param user the authenticated user the token represents
	 * @return the compact, signed JWT string
	 */
	public String generateToken(User user) {
		Date now = Date.from(Instant.now());
		// Token lifetime is one hour from issuance.
		Date expiry = Date.from(Instant.now().plus(1, ChronoUnit.HOURS));
		// Subject is the stable public id; role/email/provider are added as custom claims
		// and the
		// whole token is signed so it cannot be tampered with by the client.
		return Jwts.builder()
			.subject(user.getPublicId().toString())
			.claim("role", user.getRole().name())
			.claim("email", user.getEmail())
			.claim("provider", user.getProviderType().name())
			.issuedAt(now)
			.expiration(expiry)
			.signWith(secretKey)
			.compact();
	}

	/**
	 * Checks whether a token's signature and structure are valid.
	 * @param token the JWT to verify
	 * @return {@code true} if the token parses and verifies, {@code false} otherwise
	 */
	public boolean validateToken(String token) {
		try {
			// Parsing signed claims verifies both the signature and the expiry.
			Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);

			return true;
		}
		catch (JwtException | IllegalArgumentException e) {
			// Any parsing/verification failure means the token is not trustworthy.
			return false;
		}

	}

	/**
	 * Extracts the user's public id (the token subject) from a token.
	 * @param token the JWT to read
	 * @return the user's public id parsed from the subject claim
	 */
	public UUID extractUserId(String token) {

		Claims claims = extractClaims(token);

		// The subject was set to the user's public id at issuance time.
		return UUID.fromString(claims.getSubject());
	}

	/**
	 * Checks token validity by attempting to parse its claims.
	 * @param token the JWT to check
	 * @return {@code true} if the claims can be verified and read, {@code false}
	 * otherwise
	 */
	public boolean isTokenValid(String token) {

		try {

			extractClaims(token);

			return true;

		}
		catch (JwtException | IllegalArgumentException ex) {

			return false;
		}
	}

	/**
	 * Verifies the token signature and returns its claim set.
	 * @param token the JWT to parse
	 * @return the verified claims payload
	 * @throws io.jsonwebtoken.JwtException if the token is invalid, expired or tampered
	 * with
	 */
	public Claims extractClaims(String token) {

		// verifyWith enforces the signature before the payload is trusted and returned.
		return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
	}

}
