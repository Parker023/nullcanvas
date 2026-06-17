package com.nullcanvas.blogapp.blog.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Small shared helper utilities for the blog module.
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Component
public class BlogAppUtil {

	private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz123456789";

	private static final SecureRandom random = new SecureRandom();

	/**
	 * Generates a random UUID.
	 * @return a freshly generated random UUID
	 */
	public UUID generateRandomUUID() {
		return UUID.randomUUID();
	}

	public String generateRandomFileHash() {

		return random.ints(4, 0, ALPHABET.length()).boxed().map(Object::toString).collect(Collectors.joining());
	}

}
