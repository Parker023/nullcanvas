package com.nullcanvas.blogapp.blog.dto.request;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import lombok.Builder;
import tools.jackson.databind.JsonNode;

/**
 * Content upload carrying a blog's entire body in one request.
 *
 * @param content the full blog content as a JSON document
 * @author Anirudh
 * @since 04/06/26
 */
@Builder
public record FullContentRequest(JsonNode content) implements BlogContentRequest {
	/**
	 * @return always {@link ContentHandleType#FULL}, routing this to the full-content
	 * strategy
	 */
	@Override
	public ContentHandleType getContentHandleType() {
		return ContentHandleType.FULL;
	}
}
