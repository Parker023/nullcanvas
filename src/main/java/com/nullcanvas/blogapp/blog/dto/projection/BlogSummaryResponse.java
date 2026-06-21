package com.nullcanvas.blogapp.blog.dto.projection;

import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.dto.Visibility;
import com.nullcanvas.blogapp.blog.dto.response.TagDetailsResponse;
import io.micrometer.core.instrument.Tags;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 07/06/26
 */
public record BlogSummaryResponse(String title, String summary, String slug, String coverImageUrl, BlogStatus status,
		Visibility visibility, UUID publicId, Instant createdAt) {
}
