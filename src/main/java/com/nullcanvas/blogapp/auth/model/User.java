package com.nullcanvas.blogapp.auth.model;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.dto.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistent user account, mapped to the {@code users} table.
 *
 * <p>
 * Holds both local (email/password) and OAuth2 accounts; {@link #providerType}
 * distinguishes them and {@link #password} is only populated for local accounts. Inherits
 * identity and audit columns from {@link BaseEntity}.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

	/** The account's unique email address. */
	@Column(unique = true)
	private String email;

	/** The user's display name. */
	@Column(nullable = false)
	private String name;

	/** Hashed password; {@code null} for OAuth2 accounts that never set one. */
	@Column
	private String password;

	/** Origin of the account (LOCAL, GOOGLE, GITHUB), stored as its enum name. */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProviderType providerType;

	/**
	 * Identifier of the user at the originating provider (their own id for local
	 * accounts).
	 */
	@Column(nullable = false)
	private String providerUserId;

	/** URL of the user's avatar image, if available. */
	@Column
	private String profileImageUrl;

	/** Authorization role granted to the user, stored as its enum name. */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	/** Whether the user has confirmed ownership of their email via OTP. */
	@Column(nullable = false)
	private boolean emailVerified;

}
