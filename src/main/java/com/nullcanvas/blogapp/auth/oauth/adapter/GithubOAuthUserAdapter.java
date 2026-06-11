package com.nullcanvas.blogapp.auth.oauth.adapter;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.oauth.dto.OAuthUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * {@link OAuthUserAdapter} for GitHub, mapping GitHub's attribute keys to
 * {@link OAuthUser}.
 *
 * @author Anirudh
 * @since 26/05/26
 */
@Service
public class GithubOAuthUserAdapter implements OAuthUserAdapter {

	/**
	 * Maps GitHub's profile attributes (id, login, avatar_url, ...) into a normalised
	 * user.
	 * @param oAuth2User the GitHub user profile
	 * @return the normalised user tagged with {@link ProviderType#GITHUB}
	 */
	@Override
	public OAuthUser adapt(OAuth2User oAuth2User) {
		// Raw key/value attributes as returned by GitHub's user endpoint.
		Map<String, Object> attributes = oAuth2User.getAttributes();

		// Translate GitHub-specific keys ("id", "login", "avatar_url") into our common
		// fields.
		return OAuthUser.builder()
			.providerUserId(String.valueOf(attributes.get("id")))
			.email((String) attributes.get("email"))
			.name((String) attributes.get("login"))
			.profileImageUrl((String) attributes.get("avatar_url"))
			.providerType(ProviderType.GITHUB)
			.build();
	}

	/**
	 * @return {@link ProviderType#GITHUB}
	 */
	@Override
	public ProviderType getProviderType() {
		return ProviderType.GITHUB;
	}

}
