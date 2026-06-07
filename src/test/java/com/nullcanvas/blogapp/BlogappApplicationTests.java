package com.nullcanvas.blogapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test verifying the Spring application context starts successfully.
 */
@SpringBootTest
class BlogappApplicationTests {

	/**
	 * Fails if the application context cannot be loaded, catching wiring/configuration
	 * errors.
	 */
	@Test
	void contextLoads() {
	}

}
