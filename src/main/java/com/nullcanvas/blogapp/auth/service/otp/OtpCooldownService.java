package com.nullcanvas.blogapp.auth.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Enforces a minimum cooldown between successive OTP issuances for the same email.
 *
 * <p>
 * Uses a short-lived Redis key as a lock: while it exists, resends are refused; its TTL
 * defines the cooldown length.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OtpCooldownService {

	/** Minimum time that must pass before another OTP can be sent to the same email. */
	@Value("${app.otp.rate-limit.cooldown}")
	private Duration cooldown;

	/** Namespace prefix for the cooldown marker keys. */
	private static final String PREFIX = "auth:otp:cooldown:";

	/** Redis access for the cooldown markers. */
	private final StringRedisTemplate redisTemplate;

	/**
	 * Checks whether a new OTP may be sent and, if so, starts the cooldown window.
	 * @param email the email a resend is requested for
	 * @return {@code true} if sending is allowed (cooldown now started), {@code false} if
	 * still cooling down
	 */
	public boolean canResend(String email) {

		String key = PREFIX + email;

		// Presence of the key means a previous send is still within its cooldown.
		Boolean exists = redisTemplate.hasKey(key);

		if (Boolean.TRUE.equals(exists)) {
			return false;
		}

		// Set the cooldown marker with the configured TTL; it auto-clears when the window
		// ends.
		redisTemplate.opsForValue().set(key, "1", cooldown);

		return true;
	}

}