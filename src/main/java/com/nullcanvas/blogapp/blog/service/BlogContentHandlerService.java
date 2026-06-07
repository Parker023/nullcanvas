package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.dto.request.BlogContentRequest;
import com.nullcanvas.blogapp.blog.service.factory.BlogContentHandlerStrategyFactory;
import com.nullcanvas.blogapp.blog.service.strategy.BlogContentHandleStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Entry-point service for storing blog content that delegates to the matching handling
 * strategy.
 *
 * <p>
 * Rather than branching on the upload type, it resolves the
 * {@link BlogContentHandleStrategy} for the request's
 * {@link com.nullcanvas.blogapp.blog.dto.ContentHandleType} and lets that strategy
 * persist the content (as chunks or as a full document).
 * </p>
 *
 * @author Anirudh
 * @since 04/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogContentHandlerService {

	/** Resolves the content-handling strategy matching a request's handle type. */
	private final BlogContentHandlerStrategyFactory blogContentHandlerStrategyFactory;

	/**
	 * Routes a content upload to the strategy that knows how to store it.
	 * @param request the content payload, carrying its handle type
	 * @param blogPublicId the blog the content belongs to
	 */
	public void handleContent(BlogContentRequest request, UUID blogPublicId) {
		// Pick the strategy (chunk vs. full) based on the request's declared handle type.
		BlogContentHandleStrategy contentHandlerStrategy = blogContentHandlerStrategyFactory
			.getContentHandlerStrategy(request.getContentHandleType());
		// Delegate the actual persistence to the chosen strategy.
		contentHandlerStrategy.handle(request, blogPublicId);
	}

}
