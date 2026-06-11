package com.nullcanvas.blogapp.auth.dto.request;

import com.nullcanvas.blogapp.auth.dto.AuthType;

/**
 * Authentication request carrying email/password credentials for JWT login.
 *
 * @param email the user's email address
 * @param password the user's raw password to be verified
 * @author Anirudh
 * @since 26/05/26
 */

public record JwtAuthenticationRequest(String email, String password) implements AuthenticationRequest {

	/**
	 * @return always {@link AuthType#JWT}, routing this request to the JWT strategy
	 */
	@Override
	public AuthType getAuthType() {
		return AuthType.JWT;
	}
}
