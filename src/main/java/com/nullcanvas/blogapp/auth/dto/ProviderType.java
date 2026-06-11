package com.nullcanvas.blogapp.auth.dto;

/**
 * Identifies the origin of a user account.
 *
 * <p>
 * {@link #LOCAL} marks accounts created with email/password, while {@link #GOOGLE} and
 * {@link #GITHUB} mark accounts that signed in through the respective OAuth2 provider.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
public enum ProviderType {

	GOOGLE, GITHUB, LOCAL;

	/**
	 * Resolves an external OAuth2 provider name into its enum constant.
	 * @param type the provider name (case-insensitive), e.g. {@code "google"} or
	 * {@code "github"}
	 * @return the matching provider constant
	 * @throws IllegalArgumentException if the name maps to no supported OAuth2 provider
	 */
	public static ProviderType fromString(String type) {
		// Normalise to lower-case so the lookup is case-insensitive; LOCAL is
		// intentionally
		// excluded because it is never the source of an OAuth2 sign-in.
		return switch (type.toLowerCase()) {
			case "google" -> GOOGLE;
			case "github" -> GITHUB;
			default -> throw new IllegalArgumentException(type + " does not support");
		};
	}

}
