package com.nullcanvas.blogapp.auth.oauth.adapter;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.oauth.dto.OAuthUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * @author Anirudh
 * @since 25/05/26
 */
public interface OAuthUserAdapter {

	OAuthUser adapt(OAuth2User oAuth2User);

	ProviderType getProviderType();

}
