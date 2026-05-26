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
@RequiredArgsConstructor
@Getter
public class OAuthAuthenticationRequest implements AuthenticationRequest {

    private final OAuth2User oauth2User;
    private final ProviderType provider;

    @Override
    public AuthType getAuthType() {
        return AuthType.OAUTH2;
    }

}
