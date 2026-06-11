package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.dto.request.BlogMetadataRequest;
import com.nullcanvas.blogapp.blog.repository.BlogTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Associates tags with a blog (work in progress).
 *
 * @author Anirudh
 * @since 05/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogTagService {

	/** Persistence for blog-to-tag association rows. */
	private final BlogTagRepository blogTagRepository;

	/**
	 * Persists the tag associations for a blog.
	 * @param metadataRequest the request carrying the tag names
	 * @param blogPublicId the blog the tags should be linked to
	 */
	public void insertBlogTag(BlogMetadataRequest metadataRequest, UUID blogPublicId) {

		// TODO tags

	}

}
