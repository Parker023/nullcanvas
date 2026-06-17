package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.dto.RemoteUploadType;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 *
 */
public interface RemoteResourceManagementService {

	String upload(MultipartFile file, RemoteUploadType type, UUID blogId);

}
