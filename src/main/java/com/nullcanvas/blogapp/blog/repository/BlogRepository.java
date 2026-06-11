package com.nullcanvas.blogapp.blog.repository;

import com.nullcanvas.blogapp.blog.dto.projection.BlogSummaryResponse;
import com.nullcanvas.blogapp.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data repository for {@link Blog} entities.
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

	/**
	 * Finds a blog by its public id.
	 * @param blogPublicId the public id to look up
	 * @return the matching blog, or empty if none exists
	 */
	Optional<Blog> findByPublicId(UUID blogPublicId);

	List<BlogSummaryResponse> findAllByAuthorPublicIdOrderByCreatedAtDesc(UUID authorPublicId);

}
