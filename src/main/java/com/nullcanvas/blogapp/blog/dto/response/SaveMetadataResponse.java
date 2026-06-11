package com.nullcanvas.blogapp.blog.dto.response;

import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import lombok.Builder;

import java.util.UUID;

/**
 * Response returned after saving blog metadata, giving the client the handle for content
 * uploads.
 *
 * @param blogPublicId the public id of the newly created blog, used in subsequent content
 * calls
 * @param status the blog's current lifecycle status
 * @author Anirudh
 * @since 05/06/26
 */
@Builder
public record SaveMetadataResponse(UUID blogPublicId, BlogStatus status) {
}
