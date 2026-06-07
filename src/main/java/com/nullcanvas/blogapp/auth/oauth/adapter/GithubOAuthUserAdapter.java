package com.nullcanvas.blogapp.auth.oauth.adapter;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.oauth.dto.OAuthUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Anirudh
 * @since 26/05/26
 */
@Service
public class GithubOAuthUserAdapter implements OAuthUserAdapter {

	@Override
	public OAuthUser adapt(OAuth2User oAuth2User) {
		Map<String, Object> attributes = oAuth2User.getAttributes();

		return OAuthUser.builder()
			.providerUserId(String.valueOf(attributes.get("id")))
			.email((String) attributes.get("email"))
			.name((String) attributes.get("login"))
			.profileImageUrl((String) attributes.get("avatar_url"))
			.providerType(ProviderType.GITHUB)
			.build();
	}

	@Override
	public ProviderType getProviderType() {
		return ProviderType.GITHUB;
	}

}
