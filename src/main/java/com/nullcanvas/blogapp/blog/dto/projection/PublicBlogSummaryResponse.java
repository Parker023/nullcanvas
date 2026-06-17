package com.nullcanvas.blogapp.blog.dto.projection;

import java.time.Instant;
import java.util.UUID;

public record PublicBlogSummaryResponse(String title, String summary, String slug, String coverImageUrl, UUID publicId,
		Instant createdAt) {
}