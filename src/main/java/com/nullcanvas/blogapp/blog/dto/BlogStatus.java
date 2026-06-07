package com.nullcanvas.blogapp.blog.dto;

/**
 * Lifecycle state of a blog post.
 *
 * <p>
 * A blog typically starts {@link #IN_PROGRESS} while content is being uploaded, then
 * moves to {@link #DRAFT}, {@link #PUBLISHED} or {@link #ARCHIVED}.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */
public enum BlogStatus {

	DRAFT, ARCHIVED, PUBLISHED, IN_PROGRESS;

	/**
	 * Parses a status name into its enum constant.
	 * @param value the status name, case-insensitive
	 * @return the matching status
	 * @throws IllegalArgumentException if the name matches no known status
	 */
	public static BlogStatus fromString(String value) {
		// Lower-case so incoming values match regardless of casing.
		return switch (value.toLowerCase()) {
			case "draft" -> DRAFT;
			case "archived" -> ARCHIVED;
			case "published" -> PUBLISHED;
			case "in_progress" -> IN_PROGRESS;
			default -> throw new IllegalArgumentException("invalid status: " + value);
		};
	}

}
