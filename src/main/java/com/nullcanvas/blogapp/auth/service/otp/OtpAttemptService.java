package com.nullcanvas.blogapp.auth.service.otp;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tracks failed OTP verification attempts to throttle brute-force guessing.
 *
 * <p>
 * Each wrong guess increments a per-email counter in an in-memory cache that expires
 * after a configured window; once the counter passes the allowed maximum, further
 * attempts are disallowed until it expires.
 * </p>
 */
@Service
public class OtpAttemptService {

	/** Maximum number of failed verification attempts permitted within the window. */
	@Value("${app.otp.rate-limit.attempt.retries}")
	private int maxAttempts;

	/** How long the failed-attempt counter lives before resetting. */
	@Value("${app.otp.rate-limit.attempt.window}")
	private Duration window;

	/** Per-email failure counters that expire when the window ends. */
	private Cache<String, AtomicInteger> attemptCounters;

	/** Builds the counter cache once the configured window is available. */
	@PostConstruct
	public void init() {
		attemptCounters = Caffeine.newBuilder().expireAfterWrite(window).build();
	}

	/**
	 * Records one failed verification attempt and reports whether further attempts are
	 * allowed.
	 * @param email the email being verified
	 * @return {@code true} if the user may keep trying, {@code false} once the limit is
	 * exceeded
	 */
	public boolean recordFailure(String email) {

		// Increment the per-email failure count; the counter is created (and its window
		// started) on the first failure and reused until it expires.
		long count = attemptCounters.get(email, key -> new AtomicInteger()).incrementAndGet();

		// Allowed while the count is within the configured maximum.
		return count <= maxAttempts;
	}

	/**
	 * Clears the failure counter, called after a successful verification.
	 * @param email the email whose counter should be reset
	 */
	public void reset(String email) {

		attemptCounters.invalidate(email);
	}

}
