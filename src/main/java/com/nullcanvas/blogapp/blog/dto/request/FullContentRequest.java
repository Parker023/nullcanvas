package com.nullcanvas.blogapp.blog.dto.request;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import lombok.Builder;
import tools.jackson.databind.JsonNode;

/**
 * @author Anirudh
 * @since 04/06/26
 */
@Builder
public record FullContentRequest(JsonNode content) implements BlogContentRequest {
	@Override
	public ContentHandleType getContentHandleType() {
		return ContentHandleType.FULL;
	}
}
