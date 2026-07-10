package com.nullcanvas.blogapp.auth.service.otp;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Caps the number of OTP requests an email may make within a rolling window.
 *
 * <p>
 * Distinct from the cooldown (which spaces out individual requests), this guards against
 * a high total volume of requests over the window by counting them in an in-memory cache
 * whose entries expire when the window ends.
 * </p>
 */
@Service
public class OtpRateLimitService {

	/** Maximum number of OTP requests permitted within the window. */
	@Value("${app.otp.rate-limit.max-req}")
	private long maxRequests;

	/** Length of the rolling window over which requests are counted. */
	@Value("${app.otp.rate-limit.window}")
	private Duration window;

	/** Per-email request counters that expire when the window ends. */
	private Cache<String, AtomicInteger> requestCounters;

	/** Builds the counter cache once the configured window is available. */
	@PostConstruct
	public void init() {
		requestCounters = Caffeine.newBuilder().expireAfterWrite(window).build();
	}

	/**
	 * Records an OTP request and reports whether it is within the allowed quota.
	 * @param email the email making the request
	 * @return {@code true} if within the quota, {@code false} once the limit is exceeded
	 */
	public boolean isAllowed(String email) {

		// Count this request; the counter is created (and its window started) on the
		// first request and reused by later ones until it expires.
		long count = requestCounters.get(email, key -> new AtomicInteger()).incrementAndGet();

		// Allowed while within the configured maximum.
		return count <= maxRequests;
	}

}
