package com.nullcanvas.blogapp.blog;

import com.nullcanvas.blogapp.blog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Anirudh
 * @since 01/06/26
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}
