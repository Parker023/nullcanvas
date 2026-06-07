package com.nullcanvas.blogapp.auth.dto;

/**
 * @author Anirudh
 * @since 26/05/26
 */
public enum Role {

	USER, ADMIN;

	public static Role fromString(String value) {
		return switch (value.toLowerCase()) {
			case "user" -> USER;
			case "admin" -> ADMIN;
			default -> throw new IllegalArgumentException("Invalid type: " + value);
		};
	}

}
