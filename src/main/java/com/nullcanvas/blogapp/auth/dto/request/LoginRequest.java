package com.nullcanvas.blogapp.auth.dto.request;

import lombok.Data;

/**
 * Request body for the {@code /auth/v1/login} endpoint carrying the user's login
 * credentials.
 *
 * @author Anirudh
 * @since 28/05/26
 */
@Data
public class LoginRequest {

	/** The email address the user is logging in with. */
	private String email;

	/** The raw password supplied by the user. */
	private String password;

}
