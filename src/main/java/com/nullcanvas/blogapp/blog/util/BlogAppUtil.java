package com.nullcanvas.blogapp.blog.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 01/06/26
 */
@Component
public class BlogAppUtil {

	public UUID generateRandomUUID() {
		return UUID.randomUUID();
	}

}
