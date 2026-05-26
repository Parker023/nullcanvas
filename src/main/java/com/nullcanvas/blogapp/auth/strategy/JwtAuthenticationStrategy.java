package com.nullcanvas.blogapp.auth.strategy;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import org.springframework.stereotype.Service;

/**
 * @author Anirudh
 * @since 25/05/26
 */
@Service
public class JwtAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        return null;
    }

    @Override
    public AuthType getType() {
        return AuthType.JWT;
    }
}
