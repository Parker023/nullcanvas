package com.nullcanvas.blogapp.blog.dto;

/**
 * @author Anirudh
 * @since 01/06/26
 */
public enum Visibility {

	PUBLIC, PRIVATE;

	public static Visibility fromString(String value) {
		return switch (value.toLowerCase()) {
			case "public" -> PUBLIC;
			case "private" -> PRIVATE;
			default -> throw new IllegalArgumentException("Invalid visibility type: " + value);
		};
	}

}
