package com.nullcanvas.blogapp.blog.dto.response;

import lombok.Builder;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 18/06/26
 */
@Builder
public record TagDetailsResponse(String name, UUID tagPublicId) {
}
