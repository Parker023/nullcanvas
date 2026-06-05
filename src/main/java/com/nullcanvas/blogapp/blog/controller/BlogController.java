package com.nullcanvas.blogapp.blog.controller;

import com.nullcanvas.blogapp.blog.dto.request.BlogMetadataRequest;
import com.nullcanvas.blogapp.blog.dto.request.ChunkContentRequest;
import com.nullcanvas.blogapp.blog.dto.request.FullContentRequest;
import com.nullcanvas.blogapp.blog.dto.response.MetadataResponse;
import com.nullcanvas.blogapp.blog.service.BlogContentHandlerService;
import com.nullcanvas.blogapp.blog.service.BlogMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final BlogMetadataService blogMetadataService;
    private final BlogContentHandlerService contentHandlerService;

    @PostMapping("/metadata")
    public ResponseEntity<MetadataResponse> insertMetadata(@RequestBody BlogMetadataRequest metadataRequest) {

        MetadataResponse metadataResponse = blogMetadataService.saveMetadata(metadataRequest);

        return ResponseEntity
                .ok(metadataResponse);
    }

    @PostMapping("/{blogPublicId}/chunk")
    public ResponseEntity<Void> insertChunk(@RequestBody ChunkContentRequest chunkContentRequest, @PathVariable(name = "blogPublicId") UUID blogPublicId) {
        contentHandlerService.handleContent(chunkContentRequest,blogPublicId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/{blogPublicId}/content")
    public ResponseEntity<Void> insertFullContent(@RequestBody FullContentRequest fullContentRequest, @PathVariable(name = "blogPublicId") UUID blogPublicId) {

        contentHandlerService.handleContent(fullContentRequest,blogPublicId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
