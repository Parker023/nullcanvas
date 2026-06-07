package com.nullcanvas.blogapp.blog.dto;

/**
 * Who may view a blog: {@link #PUBLIC} for everyone or {@link #PRIVATE} for the author
 * only.
 *
 * @author Anirudh
 * @since 01/06/26
 */
public enum Visibility {

	PUBLIC, PRIVATE;

	/**
	 * Parses a visibility name into its enum constant.
	 * @param value the visibility name, case-insensitive
	 * @return the matching visibility
	 * @throws IllegalArgumentException if the name matches no known visibility
	 */
	public static Visibility fromString(String value) {
		return switch (value.toLowerCase()) {
			case "public" -> PUBLIC;
			case "private" -> PRIVATE;
			default -> throw new IllegalArgumentException("Invalid visibility type: " + value);
		};
	}

}
