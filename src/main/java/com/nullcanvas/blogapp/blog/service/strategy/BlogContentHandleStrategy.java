package com.nullcanvas.blogapp.blog.service.strategy;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import com.nullcanvas.blogapp.blog.dto.request.BlogContentRequest;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 02/06/26
 */
public interface BlogContentHandleStrategy {

	ContentHandleType getContentHandleType();

	void handle(BlogContentRequest contentRequest, UUID blogPublicId);

}
