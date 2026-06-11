package com.nullcanvas.blogapp.auth.dto;

/**
 * Supported authentication mechanisms.
 *
 * <p>
 * Used to select the matching {@code AuthenticationStrategy}: {@link #JWT} for
 * email/password login and {@link #OAUTH2} for third-party provider sign-in.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */

public enum AuthType {

	/** Email/password authentication that issues a signed JWT. */
	JWT,
	/** Delegated sign-in through an external OAuth2 provider. */
	OAUTH2

}
