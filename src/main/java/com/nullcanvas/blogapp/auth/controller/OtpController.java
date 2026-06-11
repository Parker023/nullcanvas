package com.nullcanvas.blogapp.auth.controller;

import com.nullcanvas.blogapp.auth.dto.request.ResendOtpRequest;
import com.nullcanvas.blogapp.auth.service.UserRegistrationService;
import com.nullcanvas.blogapp.auth.service.VerifyEmailRequest;
import com.nullcanvas.blogapp.auth.service.otp.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for the one-time-password (OTP) email-verification flow under
 * {@code /auth/otp/v1}.
 *
 * <p>
 * After registration a user must confirm ownership of their email by submitting the OTP
 * that was sent to them. This controller exposes verification and a resend endpoint for
 * when the original code expires or is lost.
 * </p>
 *
 * @author Anirudh
 * @since 03/06/26
 */
@RestController
@RequestMapping("/auth/otp/v1")
@RequiredArgsConstructor
public class OtpController {

	/** Marks the account verified once the submitted OTP is confirmed valid. */
	private final UserRegistrationService registrationService;

	/** Generates and re-sends OTP codes, enforcing rate-limit and cooldown rules. */
	private final OtpService otpService;

	/**
	 * Verifies the OTP a user received during registration and activates their account.
	 * @param request the email and OTP code to validate
	 * @return {@code 201 Created} once the email is successfully verified
	 */
	@PostMapping("/verify-otp")
	public ResponseEntity<Void> verify(@RequestBody @Valid VerifyEmailRequest request) {

		// Validate the OTP and mark the user's email as verified.
		registrationService.verifyEmail(request);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * Issues a fresh OTP to the user's email, subject to rate-limit and cooldown
	 * enforcement.
	 * @param request the email the new OTP should be sent to
	 * @return {@code 201 Created} once a new OTP has been dispatched
	 */
	@PostMapping("/resend-otp")
	public ResponseEntity<Void> resendOtp(@RequestBody @Valid ResendOtpRequest request) {
		// Generate and email a new OTP; the service rejects abusive resend attempts.
		otpService.resendOtp(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
