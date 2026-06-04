package com.nullcanvas.blogapp.auth.dto.request;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Anirudh
 * @since 26/05/26
 */
@RequiredArgsConstructor
@Getter
public class JwtAuthenticationRequest implements AuthenticationRequest {

    private final String email;
    private final String password;

    @Override
    public AuthType getAuthType() {
        return AuthType.JWT;
    }
}
