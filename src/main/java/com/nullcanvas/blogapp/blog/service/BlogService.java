package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.BlogRepository;
import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.dto.Visibility;
import com.nullcanvas.blogapp.blog.dto.request.BlogMetadataRequest;
import com.nullcanvas.blogapp.blog.model.Blog;
import com.nullcanvas.blogapp.blog.util.AuthUtil;
import com.nullcanvas.blogapp.blog.util.BlogAppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 01/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogService {
    private final BlogAppUtil appUtil;
    private final BlogRepository blogRepository;
    private AuthUtil authUtil;
    private SlugGeneratorService slugGeneratorService;

    public UUID generateBlogPublicId() {
        UUID blogPublicId = appUtil.generateRandomUUID();
        log.info("publicId generated {}", blogPublicId);
        return blogPublicId;
    }

    @Transactional
    public void saveMetadata(BlogMetadataRequest metadataRequest) {

        Blog blogWithMetadata = prepareBlog(metadataRequest);

        blogRepository.save(blogWithMetadata);

    }

    private Blog prepareBlog(BlogMetadataRequest metadataRequest) {
        return Blog.builder()
                .title(metadataRequest.getTitle())
                .summary(metadataRequest.getSummary())
                .authorPublicId(authUtil.getAuthorIdFromSecurityContext())
                .coverImageUrl(metadataRequest.getCoverImageUrl())
                .visibility(Visibility.fromString(metadataRequest.getVisibility()))
                .slug(slugGeneratorService.generate(metadataRequest.getTitle()))
                .status(BlogStatus.fromString(metadataRequest.getStatus()))
                .build();

    }


}
