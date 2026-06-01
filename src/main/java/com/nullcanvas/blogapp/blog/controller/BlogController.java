package com.nullcanvas.blogapp.blog.controller;

import com.nullcanvas.blogapp.blog.dto.request.BlogMetadataRequest;
import com.nullcanvas.blogapp.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 01/06/26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/v1")
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/init")
    public ResponseEntity<UUID> getBlogPublicId() {

        UUID blogPublicId = blogService.generateBlogPublicId();
        return ResponseEntity.ok(blogPublicId);

    }

    @PostMapping("/metadata")
    public ResponseEntity<Void> insertMetadata(@RequestBody BlogMetadataRequest metadataRequest) {

        blogService.saveMetadata(metadataRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
