package com.nullcanvas.blogapp.auth.factory;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.strategy.AuthenticationStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Anirudh
 * @since 26/05/26
 */
@Component
public class AuthStrategyFactory {

	private final Map<AuthType, AuthenticationStrategy> authenticationStrategyMap;

	public AuthStrategyFactory(List<AuthenticationStrategy> authenticationStrategies) {
		authenticationStrategyMap = authenticationStrategies.stream()
			.collect(Collectors.toMap(AuthenticationStrategy::getType, Function.identity()));
	}

	public AuthenticationStrategy getStrategy(AuthType authType) {
		if (!authenticationStrategyMap.containsKey(authType))
			throw new IllegalArgumentException("Invalid Auth type: " + authType);
		return authenticationStrategyMap.get(authType);
	}

}
