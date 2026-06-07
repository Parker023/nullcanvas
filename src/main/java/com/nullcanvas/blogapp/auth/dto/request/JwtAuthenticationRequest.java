package com.nullcanvas.blogapp.auth.dto.request;

import com.nullcanvas.blogapp.auth.dto.AuthType;

/**
 * @author Anirudh
 * @since 26/05/26
 */

public record JwtAuthenticationRequest(String email, String password) implements AuthenticationRequest {

	@Override
	public AuthType getAuthType() {
		return AuthType.JWT;
	}
}
