package com.nullcanvas.blogapp.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Profile of the currently authenticated user, returned by the {@code /auth/v1/me}
 * endpoint.
 *
 * @author Anirudh
 * @since 03/06/26
 */

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class CurrentUserResponse {

	/** The user's display name. */
	private String name;

	/** The user's email address. */
	private String email;

	/** The user's roles expressed as authority strings (e.g. {@code "USER"}). */
	private List<String> roles;

}
