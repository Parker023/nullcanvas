package com.nullcanvas.blogapp.auth.dto.request;

import com.nullcanvas.blogapp.auth.dto.AuthType;

/**
 * Common contract for every authentication request, regardless of mechanism.
 *
 * <p>
 * Exposing the {@link AuthType} lets the strategy factory route the request to the
 * correct {@code AuthenticationStrategy} without inspecting the concrete request type.
 * </p>
 *
 * @author Anirudh
 * @since 25/05/26
 */
public interface AuthenticationRequest {

	/**
	 * @return the authentication mechanism this request should be handled by
	 */
	AuthType getAuthType();

}
