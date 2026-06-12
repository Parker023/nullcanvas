package com.nullcanvas.blogapp.blog.controller;

import com.nullcanvas.blogapp.blog.dto.projection.BlogSummaryResponse;
import com.nullcanvas.blogapp.blog.dto.request.BlogMetadataRequest;
import com.nullcanvas.blogapp.blog.dto.request.ChunkContentRequest;
import com.nullcanvas.blogapp.blog.dto.request.FullContentRequest;
import com.nullcanvas.blogapp.blog.dto.response.BlogContentResponse;
import com.nullcanvas.blogapp.blog.dto.response.FullContentResponse;
import com.nullcanvas.blogapp.blog.dto.response.SaveMetadataResponse;
import com.nullcanvas.blogapp.blog.service.reader.BlogContentReaderOrchestrator;
import com.nullcanvas.blogapp.blog.service.writer.BlogContentHandlerService;
import com.nullcanvas.blogapp.blog.service.BlogMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * The BlogController manages blog-related operations, providing endpoints for
 * creating, retrieving, and storing blog metadata and content. The controller
 * enables seamless handling of blog storage strategies including chunked and
 * full content uploads, and facilitates reading content for user-requested blogs.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/v1")
public class BlogController {

    /**
     * Persists blog metadata and returns the generated public identifier/slug.
     */
    private final BlogMetadataService blogMetadataService;

    /**
     * Routes a content payload to the strategy that knows how to store it (chunked or
     * full).
     */
    private final BlogContentHandlerService contentHandlerService;

    /**
     * Orchestrates blog content reading operations, including retrieving entire blog
     * content or specific chunks based on request parameters. Provides abstraction over
     * content retrieval strategies for full and chunked blog content.
     */
    private final BlogContentReaderOrchestrator contentReaderOrchestrator;

    /**
     * Creates a blog's metadata record, the first step before any content is uploaded.
     *
     * @param metadataRequest the title, tags and visibility describing the blog
     * @return the saved metadata including the public id used by subsequent content calls
     */
    @PostMapping("/metadata")
    public ResponseEntity<SaveMetadataResponse> insertMetadata(@RequestBody BlogMetadataRequest metadataRequest) {

        // Persist the metadata; the response carries the public id needed to upload
        // content next.
        SaveMetadataResponse metadataResponse = blogMetadataService.saveMetadata(metadataRequest);

        return ResponseEntity.ok(metadataResponse);
    }

    /**
     * Appends a single content chunk to an existing blog.
     *
     * @param chunkContentRequest the chunk payload and its ordering information
     * @param blogPublicId        the public id of the blog the chunk belongs to
     * @return {@code 201 Created} once the chunk is stored
     */
    @PostMapping("/{blogPublicId}/chunk")
    public ResponseEntity<Void> insertChunk(@RequestBody ChunkContentRequest chunkContentRequest,
                                            @PathVariable UUID blogPublicId) {
        // Delegate to the handler, which picks the chunk strategy based on the request
        // type.
        contentHandlerService.handleContent(chunkContentRequest, blogPublicId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Stores the entire body of a blog in a single request.
     *
     * @param fullContentRequest the complete content payload
     * @param blogPublicId       the public id of the blog being filled
     * @return {@code 201 Created} once the content is stored
     */
    @PostMapping("/{blogPublicId}/content")
    public ResponseEntity<Void> insertFullContent(@RequestBody FullContentRequest fullContentRequest,
                                                  @PathVariable UUID blogPublicId) {

        // Delegate to the handler, which picks the full-content strategy based on the
        // request type.
        contentHandlerService.handleContent(fullContentRequest, blogPublicId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * Retrieves a paginated list of blog summaries authored by the current user.
     *
     * @param pageable the pagination and sorting information for retrieving the blogs
     * @return a {@code ResponseEntity} containing a page of {@code BlogSummaryResponse} objects
     */
    @GetMapping("/my-blogs")
    public ResponseEntity<Page<BlogSummaryResponse>> getMyBlogs(Pageable pageable) {
        Page<BlogSummaryResponse> blogs = blogMetadataService.getBlogMetadata(pageable);
        return ResponseEntity.ok(blogs);
    }

    /**
     * Retrieves the full content of a blog based on its public identifier and slug.
     *
     * @param blogPublicId the unique public identifier of the blog
     * @param slug         the URL-friendly string identifying the blog
     * @return a ResponseEntity containing the full blog content as a
     * {@link BlogContentResponse}
     */
    @GetMapping(value = "/content/{blogPublicId}/{slug}")
    public ResponseEntity<BlogContentResponse> getFullBlogContent(@PathVariable UUID blogPublicId,
                                                                  @PathVariable String slug) {
        FullContentResponse fullContent = contentReaderOrchestrator.getFullContent(blogPublicId);
        return ResponseEntity.ok(fullContent);
    }

}
