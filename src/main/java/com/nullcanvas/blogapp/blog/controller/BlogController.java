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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST endpoints for authoring blogs under {@code /blog/v1}.
 *
 * <p>
 * Blog creation is split into two phases: first the metadata (title, tags, visibility) is
 * saved to get a public id, then the body is uploaded either as incremental chunks or
 * as a single full payload. Content uploads are routed through
 * {@link BlogContentHandlerService}, which selects the appropriate handling strategy
 * based on the request type.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/v1")
public class BlogController {

	/** Persists blog metadata and returns the generated public identifier/slug. */
	private final BlogMetadataService blogMetadataService;

	/**
	 * Routes a content payload to the strategy that knows how to store it (chunked or
	 * full).
	 */
	private final BlogContentHandlerService contentHandlerService;

	/**
	 * Creates a blog's metadata record, the first step before any content is uploaded.
	 * @param metadataRequest the title, tags and visibility describing the blog
	 * @return the saved metadata including the public id used by subsequent content calls
	 */
	@PostMapping("/metadata")
	public ResponseEntity<MetadataResponse> insertMetadata(@RequestBody BlogMetadataRequest metadataRequest) {

		// Persist the metadata; the response carries the public id needed to upload
		// content next.
		MetadataResponse metadataResponse = blogMetadataService.saveMetadata(metadataRequest);

		return ResponseEntity.ok(metadataResponse);
	}

	/**
	 * Appends a single content chunk to an existing blog.
	 * @param chunkContentRequest the chunk payload and its ordering information
	 * @param blogPublicId the public id of the blog the chunk belongs to
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
	 * @param fullContentRequest the complete content payload
	 * @param blogPublicId the public id of the blog being filled
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
	 * Fetches a blog by its public id.
	 * @param blogPublicId the public id of the blog to retrieve
	 * @return the blog response (retrieval logic not yet implemented)
	 */
	@GetMapping("/{blogPublicId}")
	public ResponseEntity<Void> getBlog(@PathVariable UUID blogPublicId) {

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
