package com.nullcanvas.blogapp.auth.service;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Anirudh
 * @since 28/05/26
 */
public record VerifyEmailRequest(@NotBlank String otp, @NotBlank String email) {
}
