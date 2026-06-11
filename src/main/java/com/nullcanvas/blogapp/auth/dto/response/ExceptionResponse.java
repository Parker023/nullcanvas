package com.nullcanvas.blogapp.auth.dto.response;

import lombok.Builder;

/**
 * Uniform error body returned to clients when a request fails.
 *
 * @param message a human-readable description of what went wrong
 * @author Anirudh
 * @since 31/05/26
 */
@Builder
public record ExceptionResponse(String message) {
}
