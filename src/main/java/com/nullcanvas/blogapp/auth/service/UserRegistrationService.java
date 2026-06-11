package com.nullcanvas.blogapp.auth.service;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.dto.request.RegisterRequest;
import com.nullcanvas.blogapp.auth.dto.Role;
import com.nullcanvas.blogapp.auth.exception.InvalidOtpException;
import com.nullcanvas.blogapp.auth.exception.UserAlreadyExistsException;
import com.nullcanvas.blogapp.auth.model.User;
import com.nullcanvas.blogapp.auth.repository.UserRepository;
import com.nullcanvas.blogapp.auth.service.otp.OtpService;
import com.nullcanvas.blogapp.auth.util.AuthenticationAppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles local-account registration and email verification.
 *
 * <p>
 * Registration creates a not-yet-verified user and triggers an OTP email; verification
 * confirms the OTP and flips the account to verified. Password hashing is delegated to
 * {@link AuthenticationAppUtil}.
 * </p>
 *
 * @author Anirudh
 * @since 28/05/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

	/** Persistence for user accounts. */
	private final UserRepository userRepository;

	/** Provides password hashing. */
	private final AuthenticationAppUtil appUtil;

	/** Generates, sends and validates verification OTPs. */
	private final OtpService otpService;

	/**
	 * Registers a new local user and dispatches a verification OTP.
	 *
	 * <p>
	 * Runs in a transaction so the user is only persisted if the whole flow succeeds.
	 * </p>
	 * @param request the registration details
	 * @throws UserAlreadyExistsException if an account already exists for the email
	 */
	@Transactional
	public void register(RegisterRequest request) {
		log.debug("inside Registration service");
		// Reject duplicate sign-ups up front.
		if (userRepository.existsByEmail(request.email())) {
			throw new UserAlreadyExistsException();
		}
		// Map the request to a persistable, unverified user (password hashed inside
		// translate).
		User user = translate(request);

		userRepository.save(user);
		log.info("user {} is registered with null canvas", request.email());

		// Kick off the email-verification flow by sending the first OTP.
		otpService.sendVerificationOtp(request.email());

	}

	/**
	 * Verifies a user's email by checking the submitted OTP and marking the account
	 * verified.
	 * @param request the email and OTP to validate
	 * @throws InvalidOtpException if the OTP is wrong or has expired
	 */
	public void verifyEmail(VerifyEmailRequest request) {

		// Confirm the OTP matches the one stored for this email and hasn't expired.
		boolean valid = otpService.validateOtp(request.email(), request.otp());

		if (!valid) {
			throw new InvalidOtpException("OTP is not Valid/ TTL Expired");
		}

		User user = userRepository.findByEmail(request.email()).orElseThrow();

		// Idempotency: if already verified, do nothing further.
		if (user.isEmailVerified()) {
			log.info("{} email already verified", request.email());
			return;
		}

		user.setEmailVerified(true);

		userRepository.save(user);

		// Clean up the now-consumed OTP from the store.
		otpService.deleteOtp(request.email());
	}

	/**
	 * Maps a registration request into a new, unverified local {@link User} with a hashed
	 * password.
	 * @param request the registration details
	 * @return the user entity ready to be persisted
	 */
	private User translate(RegisterRequest request) {
		// New local accounts start unverified, with the USER role and a hashed password;
		// providerUserId "0" is a placeholder since there is no external provider.
		return User.builder()
			.password(appUtil.hashString(request.password()))
			.role(Role.USER)
			.emailVerified(false)
			.name(request.name())
			.email(request.email())
			.providerType(ProviderType.LOCAL)
			.profileImageUrl(null)
			.providerUserId("0")
			.build();
	}

}
