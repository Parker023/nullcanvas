package com.nullcanvas.blogapp.blog.service.writer.strategy;

import com.nullcanvas.blogapp.blog.repository.BlogRepository;
import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import com.nullcanvas.blogapp.blog.dto.request.BlogContentRequest;
import com.nullcanvas.blogapp.blog.dto.request.FullContentRequest;
import com.nullcanvas.blogapp.blog.exception.BlogNotFoundException;
import com.nullcanvas.blogapp.blog.exception.InvalidRequestTypeException;
import com.nullcanvas.blogapp.blog.model.Blog;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

/**
 * Content strategy that stores a blog's complete body in a single operation.
 *
 * <p>
 * Loads the target blog, writes the supplied JSON content and records its size. Also
 * reused by {@link ChunkRequestStrategy} once it has assembled a chunked upload into a
 * full document.
 * </p>
 *
 * @author Anirudh
 * @since 02/06/26
 */
@Service
@RequiredArgsConstructor
public class FullRequestStrategy implements BlogContentHandleStrategy {

	/** Persistence for blogs. */
	private final BlogRepository blogRepository;

	/**
	 * @return {@link ContentHandleType#FULL}
	 */
	@Override
	public ContentHandleType getContentHandleType() {
		return ContentHandleType.FULL;
	}

	/**
	 * Writes the full content onto the target blog.
	 * @param contentRequest the full content (must be a {@link FullContentRequest})
	 * @param blogPublicId the blog to update
	 * @throws InvalidRequestTypeException if the request is not a full-content request
	 * @throws BlogNotFoundException if no blog exists for the public id
	 */
	@Transactional
	@Override
	public void handle(BlogContentRequest contentRequest, UUID blogPublicId) {

		// Guard: this strategy only handles full-content requests.
		if (!(contentRequest instanceof FullContentRequest(JsonNode content)))
			throw new InvalidRequestTypeException();
		Blog blog = blogRepository.findByPublicId(blogPublicId).orElseThrow(BlogNotFoundException::new);

		// Persist the content as a JSON string and record its length for size tracking.
		blog.setContentJson(content.toString());
		blog.setContentSize((long) StringUtils.length(content.toString()));

		blogRepository.save(blog);

	}

}
