package com.nullcanvas.blogapp.blog.dto.response;

import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.dto.Visibility;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 11/06/26
 */
@Builder
public record MetadataResponse(String title, String summary, String slug, String coverImageUrl, BlogStatus status,
		Visibility visibility, UUID publicId, Instant createdAt, long contentSize) {
}
