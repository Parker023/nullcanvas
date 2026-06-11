package com.nullcanvas.blogapp.auth.service;

import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import com.nullcanvas.blogapp.auth.strategy.AuthenticationStrategy;
import com.nullcanvas.blogapp.auth.factory.AuthStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Entry-point service for authentication that delegates to the concrete authentication
 * mechanism.
 *
 * <p>
 * Rather than branching on the authentication type itself, this service resolves the
 * matching {@link AuthenticationStrategy} (e.g. JWT or OAuth2) through
 * {@link AuthStrategyFactory} and lets the strategy perform the actual credential
 * verification and token issuance.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

	/** Resolves the authentication strategy that matches a request's {@code authType}. */
	private final AuthStrategyFactory strategyFactory;

	/**
	 * Authenticates a request by delegating to the strategy that handles its
	 * authentication type.
	 * @param authenticationRequest the inbound request carrying credentials and its auth
	 * type
	 * @return the authentication result, including the issued access token
	 */
	public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

		// Pick the strategy (JWT, OAuth2, ...) that matches the request's authentication
		// type.
		AuthenticationStrategy strategy = strategyFactory.getStrategy(authenticationRequest.getAuthType());
		log.debug("Strategy selected: {}", strategy.getType());
		// Run the selected strategy, which validates credentials and produces the token
		// response.
		return strategy.authenticate(authenticationRequest);

	}

}
