package com.nullcanvas.blogapp.blog.service.tags;

import com.nullcanvas.blogapp.blog.dto.response.TagDetailsResponse;

import java.util.List;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 18/06/26
 */
public interface BlogTagManager {

	void assignTagToBlog(UUID blogId, List<UUID> tagIds);

	void removeTagFromBlog(UUID blogId, List<UUID> tagIds);

	List<TagDetailsResponse> getTagsForBlog(UUID publicId);
}
