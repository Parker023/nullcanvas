package com.nullcanvas.blogapp.blog.util;

import com.nullcanvas.blogapp.auth.security.principal.CustomUserPrincipal;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Reads the currently authenticated user's identity from the Spring Security context.
 *
 * <p>
 * Used by blog operations to attribute actions (e.g. authorship) to the logged-in user
 * without threading the user id through every call.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Component
public class AuthUtil {

	/**
	 * Returns the public id of the authenticated user.
	 * @return the current user's public id
	 * @throws BadCredentialsException if there is no authentication or the principal is
	 * not a {@link CustomUserPrincipal}
	 */
	public UUID getAuthorIdFromSecurityContext() {
		// Pull the authentication that the JWT filter placed in the context.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null)
			throw new BadCredentialsException("User not authenticated");

		// Our filter stores a CustomUserPrincipal; anything else means the caller isn't a
		// real user.
		if (authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
			return principal.getUserPublicId();
		}

		throw new BadCredentialsException("User not authenticated");
	}

}
