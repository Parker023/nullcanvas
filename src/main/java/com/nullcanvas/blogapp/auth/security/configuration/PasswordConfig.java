package com.nullcanvas.blogapp.auth.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures the password hashing algorithm used across the application.
 */
@Configuration
public class PasswordConfig {

	/**
	 * Exposes a BCrypt-based {@link PasswordEncoder} for hashing and verifying passwords.
	 *
	 * <p>
	 * BCrypt salts each hash and is deliberately slow, making stored passwords resistant
	 * to brute-force and rainbow-table attacks.
	 * </p>
	 * @return the BCrypt password encoder bean
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}