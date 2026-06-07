package com.nullcanvas.blogapp.auth.exception;

/**
 * Thrown when a user exceeds the allowed number of failed OTP verification attempts.
 *
 * <p>
 * Acts as a brute-force guard so an attacker cannot keep guessing the OTP indefinitely.
 * </p>
 *
 * @author Anirudh
 * @since 28/05/26
 */
public class TooManyFailedOtpVerificationException extends RuntimeException {

}
