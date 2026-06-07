package com.nullcanvas.blogapp.auth.strategy;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.dto.Role;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.OAuthAuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.response.UserResponse;
import com.nullcanvas.blogapp.auth.jwt.JwtService;
import com.nullcanvas.blogapp.auth.model.User;
import com.nullcanvas.blogapp.auth.oauth.adapter.OAuthUserAdapter;
import com.nullcanvas.blogapp.auth.oauth.dto.OAuthUser;
import com.nullcanvas.blogapp.auth.oauth.factory.OAuthUserAdapterFactory;
import com.nullcanvas.blogapp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Authentication strategy for OAuth2 logins, with just-in-time account provisioning.
 *
 * <p>
 * Normalises the provider profile via the matching adapter, then finds the existing user
 * by email or creates one on first sign-in (treated as already email-verified), and
 * finally issues the application's own JWT.
 * </p>
 *
 * @author Anirudh
 * @since 25/05/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthAuthenticationStrategy implements AuthenticationStrategy {

	/** Resolves the provider-specific adapter that normalises the profile. */
	private final OAuthUserAdapterFactory oAuthUserAdapterFactory;

	/** Looks up and persists user accounts. */
	private final UserRepository userRepository;

	/** Generates the signed JWT for the authenticated user. */
	private final JwtService jwtService;

	/**
	 * Authenticates an OAuth2 user, provisioning the account if it does not yet exist.
	 *
	 * <p>
	 * Transactional so the lookup-or-create of the user is atomic.
	 * </p>
	 * @param request the authentication request (an {@link OAuthAuthenticationRequest})
	 * @return the response containing the issued JWT and user details
	 */
	@Transactional
	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		log.debug("Inside OAuth strategy");

		// The factory guarantees this strategy only receives OAuth requests, so the cast
		// is safe.
		OAuthAuthenticationRequest oAuthAuthenticationRequest = (OAuthAuthenticationRequest) request;

		// Pick the adapter for the authenticating provider and normalise its raw profile.
		OAuthUserAdapter adapter = oAuthUserAdapterFactory.getAdapter(oAuthAuthenticationRequest.provider());
		log.debug("Adapter selected: {}", adapter.getProviderType());

		OAuthUser oAuthUser = adapter.adapt(oAuthAuthenticationRequest.oauth2User());

		Optional<User> optionalUser = userRepository.findByEmail(oAuthUser.getEmail());

		// First sign-in for this email: provision a new account on the fly.
		if (optionalUser.isEmpty()) {
			User translatedUser = translate(oAuthUser);
			User savedUser = userRepository.save(translatedUser);
			optionalUser = Optional.of(savedUser);
		}

		User fetchedUser = optionalUser.get();

		// Issue our own token regardless of whether the user was just created or already
		// existed.
		String token = jwtService.generateToken(fetchedUser);

		return prepareResponse(token, fetchedUser);
	}

	/**
	 * @return {@link AuthType#OAUTH2}
	 */
	@Override
	public AuthType getType() {
		return AuthType.OAUTH2;
	}

	/**
	 * Maps a normalised OAuth profile into a new {@link User}.
	 * @param oauthUser the provider-agnostic profile
	 * @return a user entity ready to be persisted
	 */
	private User translate(OAuthUser oauthUser) {
		// OAuth users have no local password and are considered email-verified by the
		// provider.
		return User.builder()
			.email(oauthUser.getEmail())
			.name(oauthUser.getName())
			.profileImageUrl(oauthUser.getProfileImageUrl())
			.providerType(oauthUser.getProviderType())
			.providerUserId(oauthUser.getProviderUserId())
			.role(Role.USER)
			.emailVerified(true)
			.password(null)
			.build();
	}

	/**
	 * Assembles the authentication response from the token and user.
	 * @param token the issued JWT
	 * @param user the authenticated user
	 * @return the combined token-and-profile response
	 */
	private AuthenticationResponse prepareResponse(String token, User user) {
		// Expose only the safe, public user fields alongside the token.
		UserResponse userResponse = UserResponse.builder()
			.email(user.getEmail())
			.publicId(user.getPublicId())
			.profileImageUrl(user.getProfileImageUrl())
			.roles(List.of(user.getRole()))
			.name(user.getName())
			.build();

		return AuthenticationResponse.builder().userResponse(userResponse).token(token).build();

	}

}
