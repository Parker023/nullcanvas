package com.nullcanvas.blogapp.auth.service.otp;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nullcanvas.blogapp.auth.dto.request.ResendOtpRequest;
import com.nullcanvas.blogapp.auth.exception.CoolDownException;
import com.nullcanvas.blogapp.auth.exception.TooManyFailedOtpVerificationException;
import com.nullcanvas.blogapp.auth.exception.TooManyOtpRequestException;
import com.nullcanvas.blogapp.auth.service.EmailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Orchestrates OTP generation, delivery and verification for email confirmation.
 *
 * <p>
 * OTPs are held in an in-process Caffeine cache keyed by email with a TTL, so they expire
 * automatically. Before issuing a code this service enforces cooldown and rate-limit
 * rules, and during verification it tracks failed attempts to defend against brute-force
 * guessing.
 * </p>
 *
 * @author Anirudh
 * @since 28/05/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

	/** How long a generated OTP remains valid before it expires from the cache. */
	@Value("${app.otp.otp-ttl}")
	private Duration ttl;

	/** In-memory store of issued OTPs, keyed by email, expiring after {@link #ttl}. */
	private Cache<String, String> otpCache;

	/** Enforces a minimum delay between successive OTP requests. */
	private final OtpCooldownService otpCooldownService;

	/** Caps how many OTPs may be requested within a window. */
	private final OtpRateLimitService otpRateLimitService;

	/** Tracks failed verification attempts to block brute-forcing. */
	private final OtpAttemptService otpAttemptService;

	/** Sends the OTP email to the user. */
	private final EmailService emailService;

	/** Builds the TTL-bound OTP cache once the configured {@link #ttl} is available. */
	@PostConstruct
	public void init() {
		otpCache = Caffeine.newBuilder().expireAfterWrite(ttl).build();
	}

	/**
	 * Generates a fresh OTP, stores it with a TTL, and emails it asynchronously.
	 * @param email the recipient's email address
	 * @throws CoolDownException if the email is still within its resend cooldown
	 */
	public void sendVerificationOtp(String email) throws CoolDownException {

		// Enforce cooldown and rate-limit before doing any work.
		validateOtpRequest(email);

		String otp = generateOtp();

		// Store the OTP keyed by email; the cache evicts it automatically after the TTL.
		otpCache.put(email, otp);

		// Send the email off the request thread so the caller isn't blocked on SMTP.
		CompletableFuture.runAsync(() -> {
			emailService.sendVerificationOtp(email, otp);
		});

	}

	/**
	 * Validates a submitted OTP against the stored value, tracking failures.
	 * @param email the email the OTP was issued for
	 * @param otp the OTP the user submitted
	 * @return {@code true} if it matches; {@code false} if it is wrong or expired
	 * @throws TooManyFailedOtpVerificationException if the failure limit is exceeded
	 */
	public boolean validateOtp(String email, String otp) {

		String storedOtp = otpCache.getIfPresent(email);

		// A missing entry means the OTP never existed or has already expired.
		if (storedOtp == null) {
			return false;
		}

		boolean valid = storedOtp.equals(otp);

		if (!valid) {

			// Record the wrong guess; recordFailure returns false once the cap is hit.
			boolean allowed = otpAttemptService.recordFailure(email);

			if (!allowed) {
				throw new TooManyFailedOtpVerificationException();
			}

			return false;
		}

		// Successful match: clear the failed-attempt counter for this email.
		otpAttemptService.reset(email);

		return true;
	}

	/**
	 * Removes a stored OTP, typically after it has been successfully consumed.
	 * @param email the email whose OTP should be deleted
	 */
	public void deleteOtp(String email) {

		otpCache.invalidate(email);
	}

	/**
	 * Generates a random six-digit OTP.
	 * @return a numeric OTP string in the range 100000–999999
	 */
	public String generateOtp() {

		// Bounds 100000..999999 guarantee exactly six digits (no leading zero).
		return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
	}

	/**
	 * Applies the cooldown and rate-limit guards before an OTP may be issued.
	 * @param email the email being checked
	 * @throws CoolDownException if the resend cooldown has not elapsed
	 * @throws TooManyOtpRequestException if the request rate limit is exceeded
	 */
	private void validateOtpRequest(String email) {

		// Block requests that come faster than the allowed cooldown.
		if (!otpCooldownService.canResend(email)) {
			throw new CoolDownException();
		}

		// Block requests once the per-window quota is exhausted.
		if (!otpRateLimitService.isAllowed(email)) {
			throw new TooManyOtpRequestException();
		}
	}

	/**
	 * Re-issues an OTP, discarding any existing one first so only the latest code is
	 * valid.
	 * @param request the email a new OTP should be sent to
	 */
	public void resendOtp(ResendOtpRequest request) {

		log.info("otp re-sending for {}", request.email());
		// Invalidate the previous OTP so a resend cannot leave two valid codes.
		otpCache.invalidate(request.email());

		sendVerificationOtp(request.email());

	}

}
