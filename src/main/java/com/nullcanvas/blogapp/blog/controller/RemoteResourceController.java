package com.nullcanvas.blogapp.blog.controller;

import com.nullcanvas.blogapp.blog.dto.RemoteUploadType;
import com.nullcanvas.blogapp.blog.dto.response.StorageResultResponse;
import com.nullcanvas.blogapp.blog.service.RemoteResourceManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 17/06/26
 */
@RestController
@RequestMapping("/v1/resources")
@RequiredArgsConstructor
@Slf4j
public class RemoteResourceController {

	private final RemoteResourceManagementService remoteResourceManagementService;

	@PostMapping("/{blogId}/upload")
	public ResponseEntity<StorageResultResponse> upload(@RequestBody MultipartFile file,
			@RequestParam RemoteUploadType type, @PathVariable UUID blogId) {
		log.info("Uploading file {} to {}", file.getName(), type);
		return ResponseEntity.ok(new StorageResultResponse(remoteResourceManagementService.upload(file, type, blogId)));
	}

}
