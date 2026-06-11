package com.nullcanvas.blogapp.auth.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Tracks failed OTP verification attempts to throttle brute-force guessing.
 *
 * <p>
 * Each wrong guess increments a per-email counter in Redis that expires after a
 * configured window; once the counter passes the allowed maximum, further attempts are
 * disallowed until it expires.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OtpAttemptService {

	/** Maximum number of failed verification attempts permitted within the window. */
	@Value("${app.otp.rate-limit.attempt.retries}")
	private int maxAttempts;

	/** How long the failed-attempt counter lives before resetting. */
	@Value("${app.otp.rate-limit.attempt.window}")
	private Duration window;

	/** Namespace prefix for the attempt-counter keys. */
	private static final String PREFIX = "auth:otp:attempts:";

	/** Redis access for the attempt counters. */
	private final StringRedisTemplate redisTemplate;

	/**
	 * Records one failed verification attempt and reports whether further attempts are
	 * allowed.
	 * @param email the email being verified
	 * @return {@code true} if the user may keep trying, {@code false} once the limit is
	 * exceeded
	 */
	public boolean recordFailure(String email) {

		String key = PREFIX + email;

		// Atomically increment the per-email failure count (creates the key at 1 on first
		// failure).
		Long count = redisTemplate.opsForValue().increment(key);

		// On the first failure, start the expiry window so the counter resets
		// automatically.
		if (count == 1) {

			redisTemplate.expire(key, window);
		}

		// Allowed while the count is within the configured maximum.
		return count <= maxAttempts;
	}

	/**
	 * Clears the failure counter, called after a successful verification.
	 * @param email the email whose counter should be reset
	 */
	public void reset(String email) {

		redisTemplate.delete(PREFIX + email);
	}

}