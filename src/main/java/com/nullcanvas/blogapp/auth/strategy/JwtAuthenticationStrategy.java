package com.nullcanvas.blogapp.auth.strategy;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.dto.request.JwtAuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.response.UserResponse;
import com.nullcanvas.blogapp.auth.exception.InvalidCredentialsException;
import com.nullcanvas.blogapp.auth.exception.UserNotRegisteredException;
import com.nullcanvas.blogapp.auth.jwt.JwtService;
import com.nullcanvas.blogapp.auth.model.User;
import com.nullcanvas.blogapp.auth.repository.UserRepository;
import com.nullcanvas.blogapp.auth.util.AuthenticationAppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Authentication strategy for local email/password login, issuing a JWT on success.
 *
 * <p>
 * Looks up the user by email, verifies the supplied password against the stored hash, and
 * on a match generates a signed token. Accounts without a password (e.g. OAuth-only) are
 * treated as unregistered for this flow.
 * </p>
 *
 * @author Anirudh
 * @since 25/05/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationStrategy implements AuthenticationStrategy {

	/** Generates the signed JWT for the authenticated user. */
	private final JwtService jwtService;

	/** Looks up the user account by email. */
	private final UserRepository userRepository;

	/** Provides password hash comparison. */
	private final AuthenticationAppUtil appUtil;

	/**
	 * Verifies email/password credentials and returns a token response.
	 * @param request the authentication request (a {@link JwtAuthenticationRequest})
	 * @return the response containing the issued JWT and user details
	 * @throws UserNotRegisteredException if no account (or no password) exists for the
	 * email
	 * @throws InvalidCredentialsException if the password does not match
	 */
	@Override
	public AuthenticationResponse authenticate(AuthenticationRequest request) {

		// The factory guarantees this strategy only receives JWT requests, so the cast is
		// safe.
		JwtAuthenticationRequest jwtAuthenticationRequest = (JwtAuthenticationRequest) request;

		User user = userRepository.findByEmail(jwtAuthenticationRequest.email())
			.orElseThrow(UserNotRegisteredException::new);

		// A null password means the account exists only via OAuth and cannot log in by
		// password.
		if (Objects.isNull(user.getPassword()))
			throw new UserNotRegisteredException();

		// Compare the raw input against the stored BCrypt hash.
		boolean passwordMatch = appUtil.matches(jwtAuthenticationRequest.password(), user.getPassword());

		if (!passwordMatch) {
			throw new InvalidCredentialsException();
		}

		log.info("{} user is authenticated", user.getEmail());

		// Credentials verified: mint a signed token for the session.
		String token = jwtService.generateToken(user);

		return prepareResponse(token, user);
	}

	/**
	 * @return {@link AuthType#JWT}
	 */
	@Override
	public AuthType getType() {
		return AuthType.JWT;
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
			.name(user.getName())
			.roles(List.of(user.getRole()))
			.profileImageUrl(user.getProfileImageUrl())
			.publicId(user.getPublicId())
			.email(user.getEmail())
			.build();

		return AuthenticationResponse.builder().userResponse(userResponse).token(token).build();
	}

}
