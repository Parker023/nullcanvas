package com.nullcanvas.blogapp.auth.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Caps the number of OTP requests an email may make within a rolling window.
 *
 * <p>
 * Distinct from the cooldown (which spaces out individual requests), this guards against
 * a high total volume of requests over the window by counting them in Redis.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class OtpRateLimitService {

	/** Maximum number of OTP requests permitted within the window. */
	@Value("${app.otp.rate-limit.max-req}")
	private long maxRequests;

	/** Length of the rolling window over which requests are counted. */
	@Value("${app.otp.rate-limit.window}")
	private Duration window;

	/** Namespace prefix for the request-counter keys. */
	private static final String PREFIX = "auth:otp:rate-limit:";

	/** Redis access for the request counters. */
	private final StringRedisTemplate redisTemplate;

	/**
	 * Records an OTP request and reports whether it is within the allowed quota.
	 * @param email the email making the request
	 * @return {@code true} if within the quota, {@code false} once the limit is exceeded
	 */
	public boolean isAllowed(String email) {

		String key = PREFIX + email;

		// Atomically count this request (key starts at 1 on the first request in the
		// window).
		Long count = redisTemplate.opsForValue().increment(key);

		// Begin the window on the first request so the count resets when it expires.
		if (count == 1) {
			redisTemplate.expire(key, window);
		}

		// Allowed while within the configured maximum.
		return count <= maxRequests;
	}

}