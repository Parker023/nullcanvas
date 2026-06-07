package com.nullcanvas.blogapp.blog.dto;

/**
 * How a blog's content is delivered, selecting the storage strategy.
 *
 * <p>
 * {@link #FULL} uploads the whole body in one request; {@link #CHUNK} streams it in
 * ordered pieces.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */
public enum ContentHandleType {

	FULL, CHUNK;

	/**
	 * Parses a handle-type name into its enum constant.
	 * @param value the handle-type name, case-insensitive
	 * @return the matching handle type
	 * @throws IllegalArgumentException if the name matches no known type
	 */
	public static ContentHandleType fromString(String value) {
		return switch (value.toLowerCase()) {
			case "full" -> FULL;
			case "chunk" -> CHUNK;
			default -> throw new IllegalArgumentException("Invalid chunk handle type: " + value);
		};

	}

}
