package com.nullcanvas.blogapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point of the NullCanvas blog application.
 *
 * <p>
 * {@code @SpringBootApplication} bootstraps component scanning, auto-configuration and
 * the embedded server, while {@code @EnableJpaAuditing} activates automatic population of
 * auditing fields (such as created/updated timestamps) on JPA entities.
 * </p>
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class BlogappApplication {

	/**
	 * Boots the Spring application context and starts the embedded web server.
	 * @param args command-line arguments forwarded to Spring Boot
	 */
	public static void main(String[] args) {
		// Hand control to Spring Boot, which builds the context and starts the server.
		SpringApplication.run(BlogappApplication.class, args);
	}

}
