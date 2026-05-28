package com.nullcanvas.blogapp.auth.service;

/**
 * @author Anirudh
 * @since 28/05/26
 */
public record VerifyEmailRequest(String otp, String email) {
}
