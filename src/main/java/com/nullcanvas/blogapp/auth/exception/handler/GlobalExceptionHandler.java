package com.nullcanvas.blogapp.auth.exception.handler;

import com.nullcanvas.blogapp.auth.dto.response.ExceptionResponse;
import com.nullcanvas.blogapp.auth.exception.CoolDownException;
import com.nullcanvas.blogapp.auth.exception.InvalidCredentialsException;
import com.nullcanvas.blogapp.auth.exception.InvalidOtpException;
import com.nullcanvas.blogapp.auth.exception.TooManyFailedOtpVerificationException;
import com.nullcanvas.blogapp.auth.exception.TooManyOtpRequestException;
import com.nullcanvas.blogapp.auth.exception.UserAlreadyExistsException;
import com.nullcanvas.blogapp.auth.exception.UserNotRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Centralised exception handler that translates domain exceptions into HTTP error
 * responses.
 *
 * <p>
 * As a {@code @ControllerAdvice}, it intercepts exceptions thrown by any controller and
 * maps each one to a consistent {@link ExceptionResponse} body and the appropriate status
 * code, so the same failure always produces the same client-facing shape.
 * </p>
 *
 * @author Anirudh
 * @since 31/05/26
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Maps a cooldown violation to {@code 429 Too Many Requests}.
	 * @param exception the cooldown exception that was raised
	 * @return a 429 response telling the caller to wait before retrying
	 */
	@ExceptionHandler(CoolDownException.class)
	public ResponseEntity<ExceptionResponse> coolDownException(CoolDownException exception) {

		ExceptionResponse response = ExceptionResponse.builder().message("please try after 30 sec").build();

		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);

	}

	/**
	 * Maps invalid login credentials to {@code 401 Unauthorized}.
	 * @param exception the invalid-credentials exception that was raised
	 * @return a 401 response with a generic credential-mismatch message
	 */
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ExceptionResponse> invalidCredentialsException(InvalidCredentialsException exception) {

		// Keep the message generic (not "wrong password") so it doesn't reveal whether
		// the email exists.
		ExceptionResponse response = ExceptionResponse.builder().message("Email or password does not match").build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

	}

	/**
	 * Maps an incorrect or expired OTP to {@code 401 Unauthorized}.
	 * @param exception the invalid-OTP exception that was raised
	 * @return a 401 response indicating the OTP did not match or has expired
	 */
	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<ExceptionResponse> invalidOtpException(InvalidOtpException exception) {

		ExceptionResponse response = ExceptionResponse.builder().message("Otp does not match or expired").build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

	}

	/**
	 * Maps excessive failed OTP verifications to {@code 429 Too Many Requests}.
	 * @param exception the too-many-failed-verifications exception that was raised
	 * @return a 429 response asking the caller to retry later
	 */
	@ExceptionHandler(TooManyFailedOtpVerificationException.class)
	public ResponseEntity<ExceptionResponse> tooManyFailedOtpVerificationException(
			TooManyFailedOtpVerificationException exception) {

		ExceptionResponse response = ExceptionResponse.builder()
			.message("Too many failed attempts, please try after sometime")
			.build();

		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);

	}

	/**
	 * Maps exceeding the OTP request rate limit to {@code 429 Too Many Requests}.
	 * @param exception the too-many-requests exception that was raised
	 * @return a 429 response asking the caller to retry later
	 */
	@ExceptionHandler(TooManyOtpRequestException.class)
	public ResponseEntity<ExceptionResponse> tooManyOtpRequestException(TooManyOtpRequestException exception) {

		ExceptionResponse response = ExceptionResponse.builder()
			.message("Too many attempts, please try after sometime")
			.build();

		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);

	}

	/**
	 * Maps a duplicate-registration attempt to {@code 409 Conflict}.
	 * @param exception the user-already-exists exception that was raised
	 * @return a 409 response indicating the email/username is already taken
	 */
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ExceptionResponse> userAlreadyExistsException(UserAlreadyExistsException exception) {

		ExceptionResponse response = ExceptionResponse.builder()
			.message("User already exists with this username or email")
			.build();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

	}

	/**
	 * Maps a reference to an unknown account to {@code 404 Not Found}.
	 * @param exception the user-not-registered exception that was raised
	 * @return a 404 response indicating no account exists for the email
	 */
	@ExceptionHandler(UserNotRegisteredException.class)
	public ResponseEntity<ExceptionResponse> userNotRegisteredException(UserNotRegisteredException exception) {

		ExceptionResponse response = ExceptionResponse.builder().message("User Not found with this email").build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

	}

}
