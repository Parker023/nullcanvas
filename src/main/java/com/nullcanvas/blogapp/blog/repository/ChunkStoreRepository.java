package com.nullcanvas.blogapp.blog.repository;

import com.nullcanvas.blogapp.blog.model.ChunkStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for {@link ChunkStore} entities.
 *
 * <p>
 * Adds queries to read a blog's chunks in sequence order (for assembly) and to purge them
 * once the content has been finalised.
 * </p>
 *
 * @author Anirudh
 * @since 05/06/26
 */
@Repository
public interface ChunkStoreRepository extends JpaRepository<ChunkStore, Long> {

	/**
	 * Returns all chunks for a blog ordered by sequence, ready to be concatenated into
	 * the body.
	 * @param blogPublicId the blog whose chunks to fetch
	 * @return the chunks ordered by {@code chunk_seq}
	 */
	@Query(value = "select * from chunk_store where blog_public_id= :blogPublicId order by chunk_seq ",
			nativeQuery = true)
	List<ChunkStore> findAllByBlogPublicId(@Param("blogPublicId") UUID blogPublicId);

	/**
	 * Deletes all chunks belonging to a blog, typically after assembly is complete.
	 * @param blogPublicId the blog whose chunks to delete
	 * @return the number of chunk rows removed
	 */
	@Modifying
	int deleteByBlogPublicId(UUID blogPublicId);

}
