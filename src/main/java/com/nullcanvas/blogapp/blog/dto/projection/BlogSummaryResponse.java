package com.nullcanvas.blogapp.blog.dto.projection;

import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.dto.Visibility;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 07/06/26
 */
public record BlogSummaryResponse(String title, String summary, String slug, String coverImageUrl, BlogStatus status,
                                  Visibility visibility, UUID publicId, Instant createdAt) {
}
