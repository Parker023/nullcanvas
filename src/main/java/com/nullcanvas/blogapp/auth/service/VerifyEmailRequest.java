package com.nullcanvas.blogapp.auth.service;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for verifying an email address by submitting the received OTP.
 *
 * @param otp the one-time password the user received; must not be blank
 * @param email the email address being verified; must not be blank
 * @author Anirudh
 * @since 28/05/26
 */
public record VerifyEmailRequest(@NotBlank String otp, @NotBlank String email) {
}
