package com.nullcanvas.blogapp.blog.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Generates URL-friendly slugs from blog titles.
 *
 * <p>
 * A slug is the lower-cased title with each run of non-alphanumeric characters collapsed
 * into a single hyphen and the leading/trailing hyphens trimmed, e.g.
 * {@code "Hello, World!" -> "hello-world"}.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Service
public class SlugGeneratorService {

	/**
	 * Converts a title into a slug.
	 * @param title the blog title
	 * @return the generated slug
	 * @throws IllegalArgumentException if the title is null/blank or has no alphanumeric
	 * character
	 */
	public String generate(String title) {

		// A blank title can't produce a meaningful slug.
		if (StringUtils.isEmpty(title) || StringUtils.isEmpty(title.trim()))
			throw new IllegalArgumentException("title cannot be empty or null");

		StringBuilder slug = new StringBuilder();
		// Tracks whether the last appended char was a hyphen, to collapse consecutive
		// separators.
		boolean previousCharIsHyphen = false;

		// Tracks whether any real (alphanumeric) character was seen at all.
		boolean charsPresent = false;
		// Normalise case and surrounding whitespace before scanning.
		title = title.toLowerCase().trim();
		for (char c : title.toCharArray()) {

			if (Character.isLetterOrDigit(c)) {
				// Keep letters/digits verbatim.
				slug.append(c);
				previousCharIsHyphen = false;
				charsPresent = true;
			}
			else if (!previousCharIsHyphen) {
				// Replace a run of separators/punctuation with a single hyphen.
				slug.append("-");
				previousCharIsHyphen = true;
			}

		}

		// Titles made entirely of punctuation yield no usable slug.
		if (!charsPresent) {
			throw new IllegalArgumentException("title should have at least one character");
		}

		// Drop a leading hyphen left by punctuation at the start of the title.
		if (!slug.isEmpty() && slug.charAt(0) == '-') {
			slug.deleteCharAt(0);
		}

		// Drop a trailing hyphen left by punctuation at the end of the title.
		if (!slug.isEmpty() && slug.charAt(slug.length() - 1) == '-') {
			slug.deleteCharAt(slug.length() - 1);
		}

		return slug.toString();

	}

}
