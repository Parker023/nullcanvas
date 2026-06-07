package com.nullcanvas.blogapp.auth.oauth.dto;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Provider-agnostic OAuth2 user profile produced by the adapters.
 *
 * <p>
 * Normalises the differing attribute shapes of each provider into one structure the
 * registration and authentication logic can consume uniformly.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUser {

	/** The user's unique id at the originating provider. */
	private String providerUserId;

	/** The user's email address as reported by the provider. */
	private String email;

	/** The user's display name as reported by the provider. */
	private String name;

	/** URL of the user's avatar at the provider. */
	private String profileImageUrl;

	/** Which provider this profile came from. */
	private ProviderType providerType;

}