package com.nullcanvas.blogapp.auth.service.otp;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Enforces a minimum cooldown between successive OTP issuances for the same email.
 *
 * <p>
 * Uses a short-lived in-memory marker as a lock: while it is present, resends are
 * refused; the cache TTL defines the cooldown length.
 * </p>
 */
@Service
public class OtpCooldownService {

	/** Minimum time that must pass before another OTP can be sent to the same email. */
	@Value("${app.otp.rate-limit.cooldown}")
	private Duration cooldown;

	/** Per-email cooldown markers that expire after {@link #cooldown}. */
	private Cache<String, Boolean> cooldownCache;

	/** Builds the cooldown cache once the configured duration is available. */
	@PostConstruct
	public void init() {
		cooldownCache = Caffeine.newBuilder().expireAfterWrite(cooldown).build();
	}

	/**
	 * Checks whether a new OTP may be sent and, if so, starts the cooldown window.
	 * @param email the email a resend is requested for
	 * @return {@code true} if sending is allowed (cooldown now started), {@code false} if
	 * still cooling down
	 */
	public boolean canResend(String email) {

		// Presence of a marker means a previous send is still within its cooldown.
		if (cooldownCache.getIfPresent(email) != null) {
			return false;
		}

		// Set the cooldown marker; it auto-clears when the window ends.
		cooldownCache.put(email, Boolean.TRUE);

		return true;
	}

}
