package com.nullcanvas.blogapp.auth.factory;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.strategy.AuthenticationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory that resolves the {@link AuthenticationStrategy} matching a given
 * {@link AuthType}.
 *
 * <p>
 * Spring injects every {@code AuthenticationStrategy} bean; the factory indexes them by
 * the auth type each one declares so lookups are O(1) and adding a new mechanism requires
 * no change here.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
@Component
public class AuthStrategyFactory {

	/** Index of strategies keyed by the authentication type each one handles. */
	private final Map<AuthType, AuthenticationStrategy> authenticationStrategyMap;

	/**
	 * Builds the type-to-strategy index from all strategies discovered by Spring.
	 * @param authenticationStrategies every {@link AuthenticationStrategy} bean in the
	 * context
	 */
	public AuthStrategyFactory(List<AuthenticationStrategy> authenticationStrategies) {
		// Collect each strategy into a map keyed by the AuthType it reports via
		// getType().
		authenticationStrategyMap = authenticationStrategies.stream()
			.collect(Collectors.toMap(AuthenticationStrategy::getType, Function.identity()));
	}

	/**
	 * Returns the strategy registered for the requested authentication type.
	 * @param authType the authentication mechanism to resolve
	 * @return the matching strategy
	 * @throws IllegalArgumentException if no strategy is registered for the type
	 */
	public AuthenticationStrategy getStrategy(AuthType authType) {
		// Fail fast if no strategy was registered for this type rather than returning
		// null.
		if (!authenticationStrategyMap.containsKey(authType))
			throw new IllegalArgumentException("Invalid Auth type: " + authType);
		return authenticationStrategyMap.get(authType);
	}

}
