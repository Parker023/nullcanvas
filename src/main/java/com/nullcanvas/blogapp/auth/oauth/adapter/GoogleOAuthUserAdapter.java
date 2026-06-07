package com.nullcanvas.blogapp.auth.oauth.adapter;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.oauth.dto.OAuthUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Anirudh
 * @since 25/05/26
 */
@Service
public class GoogleOAuthUserAdapter implements OAuthUserAdapter {

	@Override
	public OAuthUser adapt(OAuth2User oAuth2User) {
		Map<String, Object> attributes = oAuth2User.getAttributes();

		return OAuthUser.builder()
			.providerUserId((String) attributes.get("sub"))
			.email((String) attributes.get("email"))
			.name((String) attributes.get("name"))
			.profileImageUrl((String) attributes.get("picture"))
			.providerType(ProviderType.GOOGLE)
			.build();

	}

	@Override
	public ProviderType getProviderType() {
		return ProviderType.GOOGLE;
	}

}
