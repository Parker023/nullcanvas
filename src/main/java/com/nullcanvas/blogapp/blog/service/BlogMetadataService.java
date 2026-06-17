package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.dto.projection.BlogSummaryResponse;
import com.nullcanvas.blogapp.blog.dto.projection.PublicBlogSummaryResponse;
import com.nullcanvas.blogapp.blog.dto.response.SaveMetadataResponse;
import com.nullcanvas.blogapp.blog.repository.BlogRepository;
import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.dto.Visibility;
import com.nullcanvas.blogapp.blog.dto.request.BlogMetadataRequest;
import com.nullcanvas.blogapp.blog.model.Blog;
import com.nullcanvas.blogapp.blog.util.AuthUtil;
import com.nullcanvas.blogapp.blog.util.BlogAppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Persists the metadata phase of blog creation, returning the handle used for content
 * uploads.
 *
 * <p>
 * Builds a new {@link Blog} from the request, deriving the slug from the title and the
 * author from the security context, and starts it in {@link BlogStatus#IN_PROGRESS} until
 * content is added.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogMetadataService {

	@Value("${app.recent-post-count}")
	private String recentPostCount;

	/**
	 * General blog helper utilities.
	 */
	private final BlogAppUtil appUtil;

	/**
	 * Persistence for blogs.
	 */
	private final BlogRepository blogRepository;

	/**
	 * Resolves the current author's id from the security context.
	 */
	private final AuthUtil authUtil;

	/**
	 * Generates URL-friendly slugs from titles.
	 */
	private final SlugGeneratorService slugGeneratorService;

	/**
	 * Saves a new blog's metadata and returns its public id and status.
	 *
	 * <p>
	 * Transactional so the blog row is committed atomically; {@code saveAndFlush} forces
	 * the insert immediately so the generated public id is available to return.
	 * </p>
	 * @param metadataRequest the title, tags, visibility and other metadata
	 * @return the saved blog's public id and lifecycle status
	 */
	@Transactional
	public SaveMetadataResponse saveMetadata(BlogMetadataRequest metadataRequest) {

		// Translate the request into a persistable blog with derived fields populated.
		Blog blogWithMetadata = prepareBlog(metadataRequest);

		// Flush immediately so the DB-assigned public id is available for the response.
		Blog savedBlog = blogRepository.saveAndFlush(blogWithMetadata);

		return SaveMetadataResponse.builder()
			.blogPublicId(savedBlog.getPublicId())
			.status(savedBlog.getStatus())
			.build();

	}

	/**
	 * Builds a new {@link Blog} from the request, deriving slug, author and initial
	 * status.
	 * @param metadataRequest the incoming metadata
	 * @return the blog entity ready to persist
	 */
	private Blog prepareBlog(BlogMetadataRequest metadataRequest) {

		// Author is taken from the authenticated principal; slug is derived from the
		// title; the blog
		return Blog.builder()
			.title(metadataRequest.getTitle())
			.summary(metadataRequest.getSummary())
			.authorPublicId(authUtil.getAuthorIdFromSecurityContext())
			.coverImageUrl(metadataRequest.getCoverImageUrl())
			.visibility(Visibility.fromString(metadataRequest.getVisibility()))
			.slug(slugGeneratorService.generate(metadataRequest.getTitle()))
			.status(BlogStatus.fromString(metadataRequest.getStatus()))
			.build();

	}

	public Page<BlogSummaryResponse> getBlogMetadata(Pageable pageable) {
		UUID authorId = authUtil.getAuthorIdFromSecurityContext();
		log.info("Fetching blogs for author {}", authorId);
		Page<BlogSummaryResponse> metadataResponses = blogRepository
			.findAllByAuthorPublicIdOrderByCreatedAtDesc(authorId, pageable);

		log.info("Found {} blogs for author {}", metadataResponses.getTotalElements(), authorId);
		return metadataResponses;

	}

	@Transactional
	public void updateMetadata(BlogMetadataRequest metadataRequest, UUID blogPublicId) {
		log.info("Updating metadata for blog {}", blogPublicId);
		blogRepository.findByPublicId(blogPublicId).ifPresent(blog -> {
			blog.setTitle(metadataRequest.getTitle());
			blog.setSummary(metadataRequest.getSummary());
			blog.setCoverImageUrl(metadataRequest.getCoverImageUrl());
			blog.setVisibility(Visibility.fromString(metadataRequest.getVisibility()));
			blog.setStatus(BlogStatus.fromString(metadataRequest.getStatus()));
			blog.setStatus(BlogStatus.fromString(metadataRequest.getStatus()));
		});
	}

	public List<PublicBlogSummaryResponse> getRecentBlogs() {
		log.info("Fetching recent blogs");

		return blogRepository.findRecentPublicBlogs(Visibility.PUBLIC, BlogStatus.PUBLISHED,
				Limit.of(Integer.parseInt(recentPostCount)));

	}

}
