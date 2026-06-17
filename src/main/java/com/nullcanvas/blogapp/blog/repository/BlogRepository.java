package com.nullcanvas.blogapp.blog.repository;

import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.dto.Visibility;
import com.nullcanvas.blogapp.blog.dto.projection.BlogSummaryResponse;
import com.nullcanvas.blogapp.blog.dto.projection.PublicBlogSummaryResponse;
import com.nullcanvas.blogapp.blog.model.Blog;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 *
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

	/**
	 * Finds a blog by its public id.
	 * @param blogPublicId the public id to look up
	 * @return the matching blog, or empty if none exists
	 */
	Optional<Blog> findByPublicId(UUID blogPublicId);

	/**
	 * Retrieves a paginated list of blog summaries authored by a specific user, ordered
	 * by creation timestamp in descending order.
	 * @param authorPublicId the public ID of the author whose blogs are to be fetched
	 * @param pageRequest the pagination and sorting information
	 * @return a paginated list of {@link BlogSummaryResponse} containing summary
	 * information of the blogs
	 */
	Page<BlogSummaryResponse> findAllByAuthorPublicIdOrderByCreatedAtDesc(UUID authorPublicId, Pageable pageRequest);

	/**
	 * Deletes a blog by its public ID.
	 * @param publicId the unique public ID of the blog to be deleted
	 * @return the number of records deleted, typically 1 if the blog was found and
	 * deleted, or 0 if no matching blog was found
	 */
	long deleteByPublicId(UUID publicId);

	@Query("""
			    SELECT new com.nullcanvas.blogapp.blog.dto.projection.PublicBlogSummaryResponse(
			        b.title,
			        b.summary,
			        b.slug,
			        b.coverImageUrl,
			        b.publicId,
			        b.createdAt
			    )
			    FROM Blog b
			    WHERE b.visibility = :visibility
			    AND b.status = :status
			    ORDER BY b.createdAt DESC
			""")
	List<PublicBlogSummaryResponse> findRecentPublicBlogs(Visibility visibility, BlogStatus status, Limit limit);

}
