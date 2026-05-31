package com.nullcanvas.blogapp.auth.dto.response;

import lombok.Builder;

/**
 * @author Anirudh
 * @since 31/05/26
 */
@Builder
public record ExceptionResponse(String message, int status) {
}
