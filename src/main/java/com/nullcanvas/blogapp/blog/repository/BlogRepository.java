package com.nullcanvas.blogapp.blog.repository;

import com.nullcanvas.blogapp.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 01/06/26
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

	Optional<Blog> findByPublicId(UUID blogPublicId);

}
