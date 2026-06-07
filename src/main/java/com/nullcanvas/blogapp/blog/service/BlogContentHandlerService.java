package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.dto.request.BlogContentRequest;
import com.nullcanvas.blogapp.blog.service.factory.BlogContentHandlerStrategyFactory;
import com.nullcanvas.blogapp.blog.service.strategy.BlogContentHandleStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 04/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogContentHandlerService {

	private final BlogContentHandlerStrategyFactory blogContentHandlerStrategyFactory;

	public void handleContent(BlogContentRequest request, UUID blogPublicId) {
		BlogContentHandleStrategy contentHandlerStrategy = blogContentHandlerStrategyFactory
			.getContentHandlerStrategy(request.getContentHandleType());
		contentHandlerStrategy.handle(request, blogPublicId);
	}

}
