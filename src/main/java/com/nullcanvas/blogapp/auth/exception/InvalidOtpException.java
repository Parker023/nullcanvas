package com.nullcanvas.blogapp.auth.exception;

/**
 * Thrown when a submitted OTP is incorrect or has expired.
 *
 * @author Anirudh
 * @since 28/05/26
 */
public class InvalidOtpException extends RuntimeException {

	/**
	 * @param message detail explaining why the OTP was rejected
	 */
	public InvalidOtpException(String message) {
		super(message);
	}

}
