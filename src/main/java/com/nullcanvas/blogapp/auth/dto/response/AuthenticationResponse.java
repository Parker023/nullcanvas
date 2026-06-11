package com.nullcanvas.blogapp.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Result of a successful authentication, returned by every
 * {@code AuthenticationStrategy}.
 *
 * @author Anirudh
 * @since 25/05/26
 */

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class AuthenticationResponse {

	/** The signed access token issued to the authenticated user. */
	private String token;

	/** The authenticated user's profile details. */
	private UserResponse userResponse;

}
