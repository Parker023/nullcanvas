package com.nullcanvas.blogapp.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Thin helper around {@link PasswordEncoder} for hashing and verifying secrets.
 *
 * <p>
 * Centralises password handling so callers depend on this utility rather than the encoder
 * directly.
 * </p>
 *
 * @author Anirudh
 * @since 28/05/26
 */
@Component
@RequiredArgsConstructor
public class AuthenticationAppUtil {

	/** The configured (BCrypt) encoder doing the actual hashing/verification. */
	private final PasswordEncoder passwordEncoder;

	/**
	 * Hashes a raw value (e.g. a password) for safe storage.
	 * @param value the plaintext value to hash
	 * @return the salted hash
	 */
	public String hashString(String value) {
		return passwordEncoder.encode(value);
	}

	/**
	 * Checks a raw value against a previously stored hash.
	 * @param rawValue the plaintext value to verify
	 * @param encodedValue the stored hash to compare against
	 * @return {@code true} if the raw value matches the hash
	 */
	public boolean matches(String rawValue, String encodedValue) {
		return passwordEncoder.matches(rawValue, encodedValue);
	}

}
