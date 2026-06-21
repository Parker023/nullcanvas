package com.nullcanvas.blogapp.blog.dto.response;

import lombok.Builder;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 21/06/26
 */
@Builder
public record BlogShellResponse(UUID blogPublicId) {
}
