package com.nullcanvas.blogapp.blog.repository;

import com.nullcanvas.blogapp.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for {@link Tag} entities; provides standard CRUD.
 *
 * @author Anirudh
 * @since 05/06/26
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
