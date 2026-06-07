package com.nullcanvas.blogapp.auth.exception;

/**
 * Thrown when a user requests an OTP again before the mandatory cooldown window has
 * elapsed.
 *
 * @author Anirudh
 * @since 28/05/26
 */
public class CoolDownException extends RuntimeException {

}
