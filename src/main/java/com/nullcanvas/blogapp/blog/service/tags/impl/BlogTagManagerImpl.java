package com.nullcanvas.blogapp.blog.service.tags.impl;

import com.nullcanvas.blogapp.blog.dto.response.TagDetailsResponse;
import com.nullcanvas.blogapp.blog.model.BlogTag;
import com.nullcanvas.blogapp.blog.repository.BlogTagRepository;
import com.nullcanvas.blogapp.blog.service.tags.BlogTagManager;
import com.nullcanvas.blogapp.blog.service.tags.TagManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Anirudh
 * @since 18/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogTagManagerImpl implements BlogTagManager {

    private final BlogTagRepository blogTagRepository;
    private final TagManagementService tagManagementService;

    @Override
    @Transactional
    public void assignTagToBlog(UUID blogId, List<UUID> tagIds) {
        log.info("Assigning tags {} to blog {}", tagIds, blogId);

        // get the tags already assigned to the blog
        List<BlogTag> blogTagsList = blogTagRepository.findAllByBlogPublicIdAndTagPublicIdIn(blogId, tagIds);

        List<UUID> tagIdsInDb = blogTagsList.stream().map(BlogTag::getTagPublicId).toList();
        log.info("Tags in db {} for blog {}", tagIdsInDb, blogId);

        // get the tag ids that are not in the db
        Collection<UUID> toBeAddedTags = CollectionUtils.subtract(tagIds, tagIdsInDb);
        // get the blog tags that are assigned to the blog
        List<BlogTag> blogTagsByBlogId = blogTagRepository.findAllByBlogPublicId(blogId);
        // get the tag ids that are not assigned to the blog after the update
        Collection<UUID> toBeRemovedTags = CollectionUtils
                .subtract(blogTagsByBlogId.stream().map(BlogTag::getTagPublicId).toList(), tagIds);

        CompletableFuture<Void> blogTagDeletes = CompletableFuture
                .runAsync(() -> blogTagRepository.deleteAllByBlogPublicIdAndTagPublicIdIn(blogId, toBeRemovedTags));

        // save the new tags into BlogTag table
        toBeAddedTags.forEach(
                tagId -> blogTagRepository.save(BlogTag.builder().blogPublicId(blogId).tagPublicId(tagId).build()));

        // wait for to delete to complete
        blogTagDeletes.join();


        tagManagementService.handleUsage(tagIds);

        // cleanup memory

        blogTagsList.clear();
        blogTagsByBlogId.clear();
        toBeAddedTags.clear();
        toBeRemovedTags.clear();

    }

    @Override
    @Transactional
    public void removeTagFromBlog(UUID blogId, List<UUID> tagIds) {
        log.info("Removing tags {} from blog {}", tagIds, blogId);
        blogTagRepository.deleteAllByBlogPublicIdAndTagPublicIdIn(blogId, tagIds);
        log.info("Removed tags {} from blog {}", tagIds, blogId);

    }

    @Override
    public List<TagDetailsResponse> getTagsForBlog(UUID publicId) {

        return blogTagRepository.getTagsForBlog(publicId)
                .stream().map(tagProjection -> TagDetailsResponse.builder()
                        .name(tagProjection.getTagName())
                        .tagPublicId(tagProjection.getPublicId())
                        .build())
                .toList();
    }

}
