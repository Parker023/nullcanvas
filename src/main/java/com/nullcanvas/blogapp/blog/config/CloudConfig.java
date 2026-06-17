package com.nullcanvas.blogapp.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * @author Anirudh
 * @since 17/06/26
 */
@Configuration
public class CloudConfig {

	@Bean
	public S3Client s3Client() {

		return S3Client.builder().region(Region.AP_SOUTH_1).build();

	}

}
