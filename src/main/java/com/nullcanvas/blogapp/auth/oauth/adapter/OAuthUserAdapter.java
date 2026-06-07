package com.nullcanvas.blogapp.auth.oauth.adapter;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.oauth.dto.OAuthUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Adapts a provider-specific {@link OAuth2User} into the application's normalised
 * {@link OAuthUser}.
 *
 * <p>
 * Each provider (Google, GitHub, ...) exposes its profile under different attribute keys;
 * an implementation per provider hides those differences from the rest of the auth code.
 * </p>
 *
 * @author Anirudh
 * @since 25/05/26
 */
public interface OAuthUserAdapter {

	/**
	 * Converts the provider's raw user attributes into a normalised {@link OAuthUser}.
	 * @param oAuth2User the user profile returned by the provider
	 * @return the normalised user
	 */
	OAuthUser adapt(OAuth2User oAuth2User);

	/**
	 * @return the provider this adapter handles, used by the factory to register it
	 */
	ProviderType getProviderType();

}
