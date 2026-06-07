package com.nullcanvas.blogapp.auth.oauth.adapter;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.oauth.dto.OAuthUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * {@link OAuthUserAdapter} for Google, mapping Google's attribute keys to
 * {@link OAuthUser}.
 *
 * @author Anirudh
 * @since 25/05/26
 */
@Service
public class GoogleOAuthUserAdapter implements OAuthUserAdapter {

	/**
	 * Maps Google's profile attributes (sub, name, picture, ...) into a normalised user.
	 * @param oAuth2User the Google user profile
	 * @return the normalised user tagged with {@link ProviderType#GOOGLE}
	 */
	@Override
	public OAuthUser adapt(OAuth2User oAuth2User) {
		// Raw key/value attributes as returned by Google's userinfo endpoint.
		Map<String, Object> attributes = oAuth2User.getAttributes();

		// Translate Google-specific keys ("sub", "name", "picture") into our common
		// fields.
		return OAuthUser.builder()
			.providerUserId((String) attributes.get("sub"))
			.email((String) attributes.get("email"))
			.name((String) attributes.get("name"))
			.profileImageUrl((String) attributes.get("picture"))
			.providerType(ProviderType.GOOGLE)
			.build();

	}

	/**
	 * @return {@link ProviderType#GOOGLE}
	 */
	@Override
	public ProviderType getProviderType() {
		return ProviderType.GOOGLE;
	}

}
