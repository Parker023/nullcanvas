package com.nullcanvas.blogapp.blog.repository;

import com.nullcanvas.blogapp.blog.dto.projection.TagProjection;
import com.nullcanvas.blogapp.blog.model.BlogTag;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for {@link BlogTag} join rows; provides standard CRUD.
 *
 * @author Anirudh
 * @since 05/06/26
 */
@Repository
public interface BlogTagRepository extends JpaRepository<BlogTag, Long> {

	List<BlogTag> findAllByBlogPublicIdAndTagPublicIdIn(UUID blogPublicId, Collection<UUID> tagPublicIds);

	int deleteAllByBlogPublicIdAndTagPublicIdIn(UUID blogPublicId, Collection<UUID> tagPublicIds);

	List<BlogTag> findAllByBlogPublicId(UUID blogPublicId);

	@Query(value = "select t.tag_name, t.public_id from tags t join blog_tag bt on bt.tag_public_id=t.public_id where bt.blog_public_id=:blogId",
			nativeQuery = true)
	List<TagProjection> getTagsForBlog(@Param("blogId") UUID blogId);

}
