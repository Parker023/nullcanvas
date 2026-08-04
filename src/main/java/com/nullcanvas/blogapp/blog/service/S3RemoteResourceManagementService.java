package com.nullcanvas.blogapp.blog.service;

import com.nullcanvas.blogapp.blog.dto.RemoteUploadType;
import com.nullcanvas.blogapp.blog.repository.BlogRepository;
import com.nullcanvas.blogapp.blog.util.BlogAppUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 17/06/26
 */
@Service("S3")
@RequiredArgsConstructor
@Slf4j
@Primary
public class S3RemoteResourceManagementService implements RemoteResourceManagementService {

	@Value("${app.r2.bucket-name}")
	private String bucketName;

	/**
	 * Public base URL that uploaded objects are served from (an R2 custom domain), with
	 * no trailing slash. Kept separate from the S3 API endpoint, which is not public.
	 */
	@Value("${app.r2.public-base-url}")
	private String publicBaseUrl;

	private final S3Client s3Client;

	private final BlogAppUtil appUtil;

	private final BlogRepository blogRepository;

	@Override
	@Transactional
	public String upload(MultipartFile file, RemoteUploadType type, UUID blogId) {
		log.info("Uploading file {} to S3", file.getName());

		String partition = geyS3Partition(file, blogId, type);

		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(partition)
			.contentType(file.getContentType())
			.build();

		try {
			s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

			// Serve from the public base URL (R2 custom domain) rather than the storage
			// API endpoint, which is not publicly readable.
			String publicUrl = String.format("%s/%s", publicBaseUrl, partition);

			blogRepository.findByPublicId(blogId).ifPresent(blog -> {
				blog.setCoverImageUrl(publicUrl);
			});
			return publicUrl;
		}
		catch (IOException e) {
			log.error("Error uploading file to object storage", e);
			throw new RuntimeException(e);
		}
	}

	private String geyS3Partition(MultipartFile file, UUID blogId, RemoteUploadType type) {
		String partition = "blogs" + File.separator + blogId.toString() + File.separator;
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		String fileHash = appUtil.generateRandomFileHash();
		if (type == RemoteUploadType.COVER_IMAGE) {
			return partition + "cover_image" + fileHash + "." + extension;
		}
		else {
			return partition + "content_image" + fileHash + "." + extension;
		}

	}

}
