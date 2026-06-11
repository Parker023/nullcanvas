package com.nullcanvas.blogapp.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body for re-sending an OTP, identifying the account by email.
 *
 * @param email the email address a fresh OTP should be dispatched to; must not be blank
 * @author Anirudh
 * @since 03/06/26
 */
public record ResendOtpRequest(@NotBlank String email) {
}
