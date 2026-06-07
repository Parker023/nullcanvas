package com.nullcanvas.blogapp.blog.dto.request;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;

/**
 * Common contract for blog-content uploads, regardless of how the content is delivered.
 *
 * <p>
 * Exposing the {@link ContentHandleType} lets the strategy factory route the request to
 * the right handler (chunked or full) without inspecting the concrete request type.
 * </p>
 *
 * @author Anirudh
 * @since 04/06/26
 */
public interface BlogContentRequest {

	/**
	 * @return how this content should be handled (chunked vs full)
	 */
	ContentHandleType getContentHandleType();

}
