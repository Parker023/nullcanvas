package com.nullcanvas.blogapp.auth.exception;

/**
 * Thrown when a user requests more OTPs than the rate limit permits within the allowed
 * window.
 *
 * @author Anirudh
 * @since 28/05/26
 */
public class TooManyOtpRequestException extends RuntimeException {

}
