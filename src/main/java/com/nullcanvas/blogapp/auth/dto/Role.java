package com.nullcanvas.blogapp.auth.dto;

/**
 * Authorization roles a user can hold.
 *
 * <p>
 * {@link #USER} is the default role granted to every account; {@link #ADMIN} carries
 * elevated privileges. These values back the Spring Security granted authorities.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
public enum Role {

	USER, ADMIN;

	/**
	 * Parses a role name (e.g. from a JWT claim or persisted value) into its enum
	 * constant.
	 * @param value the role name, case-insensitive
	 * @return the matching role constant
	 * @throws IllegalArgumentException if the name matches no known role
	 */
	public static Role fromString(String value) {
		// Lower-case the input so stored/claim values match regardless of casing.
		return switch (value.toLowerCase()) {
			case "user" -> USER;
			case "admin" -> ADMIN;
			default -> throw new IllegalArgumentException("Invalid type: " + value);
		};
	}

}
