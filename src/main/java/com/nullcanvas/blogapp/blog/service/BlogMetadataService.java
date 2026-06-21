package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.dto.projection.BlogSummaryResponse;
import com.nullcanvas.blogapp.blog.dto.projection.PublicBlogSummaryResponse;
import com.nullcanvas.blogapp.blog.dto.response.BlogShellResponse;
import com.nullcanvas.blogapp.blog.dto.response.SaveMetadataResponse;
import com.nullcanvas.blogapp.blog.exception.BlogNotFoundException;
import com.nullcanvas.blogapp.blog.repository.BlogRepository;
import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.dto.Visibility;
import com.nullcanvas.blogapp.blog.dto.request.BlogMetadataRequest;
import com.nullcanvas.blogapp.blog.model.Blog;
import com.nullcanvas.blogapp.blog.service.tags.BlogTagManager;
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
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

	private final BlogTagManager blogTagManager;

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
	public SaveMetadataResponse saveMetadata(BlogMetadataRequest metadataRequest, UUID blogPublicId) {

		// Translate the request into a persistable blog with derived fields populated.
		Blog blog = blogRepository.findByPublicId(blogPublicId).orElseThrow(BlogNotFoundException::new);

		blog.setTitle(metadataRequest.getTitle());
		blog.setSummary(metadataRequest.getSummary());
		blog.setCoverImageUrl(metadataRequest.getCoverImageUrl());
		blog.setVisibility(Visibility.fromString(metadataRequest.getVisibility()));
		blog.setStatus(BlogStatus.fromString(metadataRequest.getStatus()));
		blog.setInitializedDraft(false);
		blog.setSlug(slugGeneratorService.generate(metadataRequest.getTitle()));

		CompletableFuture.runAsync(() -> blogTagManager.assignTagToBlog(blog.getPublicId(), metadataRequest.getTags()))
			.join();

		return SaveMetadataResponse.builder().blogPublicId(blog.getPublicId()).status(blog.getStatus()).build();

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
		});
	}

	public List<PublicBlogSummaryResponse> getRecentBlogs() {
		log.info("Fetching recent blogs");

		return blogRepository.findRecentPublicBlogs(Visibility.PUBLIC, BlogStatus.PUBLISHED,
				Limit.of(Integer.parseInt(recentPostCount)));

	}

	@Transactional
	public BlogShellResponse createBlogShell() {
		Blog blog = Blog.builder()
			.status(BlogStatus.DRAFT)
			.visibility(Visibility.PRIVATE)
			.authorPublicId(authUtil.getAuthorIdFromSecurityContext())
			.slug("empty-title")
			.initializedDraft(true)
			.build();

		blogRepository.save(blog);
		log.info("Created blog shell {}", blog.getPublicId());

		return BlogShellResponse.builder().blogPublicId(blog.getPublicId()).build();

	}

}
