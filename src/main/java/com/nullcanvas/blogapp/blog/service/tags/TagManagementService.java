package com.nullcanvas.blogapp.blog.service.tags;

import com.nullcanvas.blogapp.blog.dto.response.TagDetailsResponse;

import java.util.List;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 18/06/26
 */
public interface TagManagementService {

    TagDetailsResponse createTag(String tagName);

    void deleteTag(UUID tagPublicId);

    List<TagDetailsResponse> getTags();

    void handleUsage(List<UUID> tagPublicIds);

}
