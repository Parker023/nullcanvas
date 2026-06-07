package com.nullcanvas.blogapp.blog.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Small shared helper utilities for the blog module.
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Component
public class BlogAppUtil {

	/**
	 * Generates a random UUID.
	 * @return a freshly generated random UUID
	 */
	public UUID generateRandomUUID() {
		return UUID.randomUUID();
	}

}
