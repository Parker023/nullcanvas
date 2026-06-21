package com.nullcanvas.blogapp.blog.controller;

import com.nullcanvas.blogapp.blog.dto.request.CreateTagRequest;
import com.nullcanvas.blogapp.blog.dto.response.TagDetailsResponse;
import com.nullcanvas.blogapp.blog.service.tags.TagManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 18/06/26
 */
@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
public class TagController {

	private final TagManagementService tagManagementService;

	@GetMapping("/all")
	public ResponseEntity<List<TagDetailsResponse>> getTags() {

		List<TagDetailsResponse> tags = tagManagementService.getTags();

		return ResponseEntity.ok(tags);

	}

	@PostMapping
	public ResponseEntity<TagDetailsResponse> createTag(@RequestBody CreateTagRequest createTagRequest) {
		return ResponseEntity.ok(tagManagementService.createTag(createTagRequest));
	}

	@DeleteMapping("/{tagId}")
	public ResponseEntity<Void> deleteTag(@PathVariable UUID tagId) {
		tagManagementService.deleteTag(tagId);
		return ResponseEntity.noContent().build();
	}

}
