package com.nullcanvas.blogapp.auth.strategy;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;

/**
 * @author Anirudh
 * @since 25/05/26
 */
public interface AuthenticationStrategy {

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthType getType();
}
