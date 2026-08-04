package com.nullcanvas.blogapp.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

/**
 * Object-storage client configuration.
 *
 * <p>
 * Storage is Cloudflare R2, which is S3-compatible, so the AWS SDK's {@link S3Client} is
 * reused against R2's endpoint. R2 requires path-style access and has no notion of
 * regions (the SDK still needs one to sign with, hence {@code auto}).
 * </p>
 *
 * @author Anirudh
 * @since 17/06/26
 */
@Configuration
public class CloudConfig {

	/**
	 * R2 S3-compatible endpoint, e.g.
	 * https://&lt;account-id&gt;.r2.cloudflarestorage.com.
	 */
	@Value("${app.r2.endpoint}")
	private String endpoint;

	/** R2 API token access key id. */
	@Value("${app.r2.access-key}")
	private String accessKey;

	/** R2 API token secret access key. */
	@Value("${app.r2.secret-key}")
	private String secretKey;

	/**
	 * Builds the S3-compatible client pointed at Cloudflare R2.
	 * @return a client configured for R2's endpoint, credentials and addressing style
	 */
	@Bean
	public S3Client s3Client() {

		return S3Client.builder()
			.endpointOverride(URI.create(endpoint))
			// R2 is not region-partitioned, but the SDK requires a region to sign with.
			.region(Region.of("auto"))
			.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
			// R2 only supports path-style addressing, not virtual-hosted-style.
			.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
			.build();

	}

}
