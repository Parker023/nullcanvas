package com.nullcanvas.blogapp.blog.repository;

import com.nullcanvas.blogapp.blog.model.ChunkStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 05/06/26
 */
@Repository
public interface ChunkStoreRepository extends JpaRepository<ChunkStore, Long> {

    @Query(value = "select * from chunk_store where blog_public_id= :blogPublicId order by chunk_seq ", nativeQuery = true)
    List<ChunkStore> findAllByBlogPublicId(@Param("blogPublicId") UUID blogPublicId);
}
