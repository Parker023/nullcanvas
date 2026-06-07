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
 * @author Anirudh
 * @since 31/05/26
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CoolDownException.class)
	public ResponseEntity<ExceptionResponse> coolDownException(CoolDownException exception) {

		ExceptionResponse response = ExceptionResponse.builder().message("please try after 30 sec").build();

		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);

	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ExceptionResponse> invalidCredentialsException(InvalidCredentialsException exception) {

		ExceptionResponse response = ExceptionResponse.builder().message("Email or password does not match").build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

	}

	@ExceptionHandler(InvalidOtpException.class)
	public ResponseEntity<ExceptionResponse> invalidOtpException(InvalidOtpException exception) {

		ExceptionResponse response = ExceptionResponse.builder().message("Otp does not match or expired").build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

	}

	@ExceptionHandler(TooManyFailedOtpVerificationException.class)
	public ResponseEntity<ExceptionResponse> tooManyFailedOtpVerificationException(
			TooManyFailedOtpVerificationException exception) {

		ExceptionResponse response = ExceptionResponse.builder()
			.message("Too many failed attempts, please try after sometime")
			.build();

		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);

	}

	@ExceptionHandler(TooManyOtpRequestException.class)
	public ResponseEntity<ExceptionResponse> tooManyOtpRequestException(TooManyOtpRequestException exception) {

		ExceptionResponse response = ExceptionResponse.builder()
			.message("Too many attempts, please try after sometime")
			.build();

		return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);

	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ExceptionResponse> userAlreadyExistsException(UserAlreadyExistsException exception) {

		ExceptionResponse response = ExceptionResponse.builder()
			.message("User already exists with this username or email")
			.build();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

	}

	@ExceptionHandler(UserNotRegisteredException.class)
	public ResponseEntity<ExceptionResponse> userNotRegisteredException(UserNotRegisteredException exception) {

		ExceptionResponse response = ExceptionResponse.builder().message("User Not found with this email").build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

	}

}
