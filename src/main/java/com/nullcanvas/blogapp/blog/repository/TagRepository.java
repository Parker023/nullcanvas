package com.nullcanvas.blogapp.blog.repository;

import com.nullcanvas.blogapp.blog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data repository for {@link Tag} entities; provides standard CRUD.
 *
 * @author Anirudh
 * @since 05/06/26
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

	boolean existsByName(String name);

	int deleteByPublicId(UUID publicId);

	List<Tag> findAllByPublicIdIn(List<UUID> publicIds);

}
