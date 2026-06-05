package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.dto.request.BlogMetadataRequest;
import com.nullcanvas.blogapp.blog.repository.BlogTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 05/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogTagService {
    private final BlogTagRepository blogTagRepository;

    public void insertBlogTag(BlogMetadataRequest metadataRequest, UUID blogPublicId) {

        //TODO tags


    }
}
