package com.nullcanvas.blogapp.blog.dto.response;

import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import lombok.Builder;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 05/06/26
 */
@Builder
public record MetadataResponse(UUID blogPublicId, BlogStatus status) {
}
