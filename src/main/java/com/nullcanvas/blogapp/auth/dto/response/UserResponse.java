package com.nullcanvas.blogapp.auth.dto.response;

import com.nullcanvas.blogapp.auth.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Public-facing view of a user account, free of sensitive fields like the password hash.
 *
 * @author Anirudh
 * @since 26/05/26
 */

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class UserResponse {

	/**
	 * Stable, externally-shareable identifier for the user (not the database primary
	 * key).
	 */
	private UUID publicId;

	/** The user's display name. */
	private String name;

	/** The user's email address. */
	private String email;

	/** The roles granted to the user. */
	private List<Role> roles;

	/** URL of the user's avatar image, if any. */
	private String profileImageUrl;

}
