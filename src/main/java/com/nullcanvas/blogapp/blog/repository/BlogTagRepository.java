package com.nullcanvas.blogapp.blog.repository;

import com.nullcanvas.blogapp.blog.model.BlogTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Anirudh
 * @since 05/06/26
 */
@Repository
public interface BlogTagRepository extends JpaRepository<BlogTag, Long> {

}
