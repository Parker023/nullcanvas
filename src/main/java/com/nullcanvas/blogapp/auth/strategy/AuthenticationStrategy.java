package com.nullcanvas.blogapp.auth.strategy;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;

/**
 * Strategy contract for a single authentication mechanism.
 *
 * <p>
 * Each implementation verifies credentials its own way (e.g. password check or OAuth
 * handshake) and returns a token-bearing response. {@link #getType()} lets the factory
 * register the strategy under the {@link AuthType} it handles.
 * </p>
 *
 * @author Anirudh
 * @since 25/05/26
 */
public interface AuthenticationStrategy {

	/**
	 * Authenticates the request and produces a token response.
	 * @param request the authentication request to process
	 * @return the authentication result including the issued token
	 */
	AuthenticationResponse authenticate(AuthenticationRequest request);

	/**
	 * @return the authentication type this strategy handles
	 */
	AuthType getType();

}
