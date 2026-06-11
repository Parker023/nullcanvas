package com.nullcanvas.blogapp.blog.service.writer.strategy;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import com.nullcanvas.blogapp.blog.dto.request.BlogContentRequest;

import java.util.UUID;

/**
 * Strategy contract for storing a blog's content in one particular way (chunked or full).
 *
 * <p>
 * {@link #getContentHandleType()} lets the factory register the strategy under the handle
 * type it supports; {@link #handle} performs the actual persistence.
 * </p>
 *
 * @author Anirudh
 * @since 02/06/26
 */
public interface BlogContentHandleStrategy {

	/**
	 * @return the content handle type this strategy supports
	 */
	ContentHandleType getContentHandleType();

	/**
	 * Stores the given content for the specified blog.
	 * @param contentRequest the content payload to persist
	 * @param blogPublicId the blog the content belongs to
	 */
	void handle(BlogContentRequest contentRequest, UUID blogPublicId);

}
