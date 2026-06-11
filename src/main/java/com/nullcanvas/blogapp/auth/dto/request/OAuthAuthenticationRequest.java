package com.nullcanvas.blogapp.auth.dto.request;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.dto.ProviderType;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Authentication request produced after a successful OAuth2 provider handshake.
 *
 * @param oauth2User the user profile returned by the provider
 * @param provider which provider authenticated the user (e.g. Google, GitHub)
 * @author Anirudh
 * @since 26/05/26
 */

public record OAuthAuthenticationRequest(OAuth2User oauth2User,
		ProviderType provider) implements AuthenticationRequest {

	/**
	 * @return always {@link AuthType#OAUTH2}, routing this request to the OAuth strategy
	 */
	@Override
	public AuthType getAuthType() {
		return AuthType.OAUTH2;
	}

}
