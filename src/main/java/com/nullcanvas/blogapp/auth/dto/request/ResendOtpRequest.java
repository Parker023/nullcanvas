package com.nullcanvas.blogapp.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Anirudh
 * @since 03/06/26
 */
public record ResendOtpRequest(@NotBlank String email) {
}
