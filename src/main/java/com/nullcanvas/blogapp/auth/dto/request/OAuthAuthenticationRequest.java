package com.nullcanvas.blogapp.auth.dto.request;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.dto.ProviderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * @author Anirudh
 * @since 26/05/26
 */

public record OAuthAuthenticationRequest(OAuth2User oauth2User,
                                         ProviderType provider) implements AuthenticationRequest {

    @Override
    public AuthType getAuthType() {
        return AuthType.OAUTH2;
    }

}
