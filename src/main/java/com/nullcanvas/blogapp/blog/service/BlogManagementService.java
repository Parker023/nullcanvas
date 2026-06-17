package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 13/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogManagementService {

	private final BlogRepository blogRepository;

	@Transactional
	public void deleteBlog(UUID blogId) {
		log.info("Deleting blog with id {}", blogId);
		long deletedRoes = blogRepository.deleteByPublicId(blogId);

		log.info("Blog with id {} deleted", blogId);
	}

	@Transactional
	public void updateBlog(UUID blogId) {
		log.info("Updating blog with id {}", blogId);

	}

}
