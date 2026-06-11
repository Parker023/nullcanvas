package com.nullcanvas.blogapp.auth.security.principal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

/**
 * Spring Security {@link UserDetails} implementation backed by the application's user
 * data.
 *
 * <p>
 * Carries the user's public id, email and authorities so they are available from the
 * security context. The account-status checks always return {@code true} because account
 * state is governed by the token's validity rather than per-flag locking.
 * </p>
 *
 * @author Anirudh
 * @since 28/05/26
 */

@Getter
@RequiredArgsConstructor
public class CustomUserPrincipal implements UserDetails {

	/** Stable public identifier of the user. */
	private final UUID userPublicId;

	/** The user's email, also used as the Spring Security username. */
	private final String email;

	/** Password hash; typically {@code null} when built from a verified token. */
	private final String password;

	/** Authorities (roles) granted to the user. */
	private final Collection<? extends GrantedAuthority> authorities;

	/**
	 * @return the email, used as the principal's username
	 */
	@Override
	public String getUsername() {
		return email;
	}

	/**
	 * @return always {@code true}; account expiry is not tracked
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * @return always {@code true}; account locking is not tracked
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * @return always {@code true}; credential expiry is not tracked
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * @return always {@code true}; the account is considered enabled
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

}
