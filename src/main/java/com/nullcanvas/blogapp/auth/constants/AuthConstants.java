package com.nullcanvas.blogapp.auth.constants;

/**
 * Shared string constants used across the authentication layer.
 *
 * <p>
 * Centralising these names avoids typos and keeps the cookie name and JWT claim keys
 * consistent wherever tokens are written or read.
 * </p>
 *
 * @author Anirudh
 * @since 28/05/26
 */
public class AuthConstants {

	/** Name of the cookie that carries the signed access token. */
	public final static String ACCESS_TOKEN = "access_token";

	/** JWT claim key under which the user's role is stored. */
	public final static String ROLE = "role";

	/** JWT claim key under which the user's email is stored. */
	public final static String EMAIL = "email";

}
