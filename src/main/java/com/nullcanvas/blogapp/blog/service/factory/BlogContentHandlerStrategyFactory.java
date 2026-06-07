package com.nullcanvas.blogapp.blog.service.factory;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import com.nullcanvas.blogapp.blog.service.strategy.BlogContentHandleStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Factory that resolves the {@link BlogContentHandleStrategy} matching a
 * {@link ContentHandleType}.
 *
 * <p>
 * Spring injects every strategy bean; the factory indexes them by the handle type each
 * one declares so a new upload mode can be added simply by contributing another strategy.
 * </p>
 *
 * @author Anirudh
 * @since 04/06/26
 */
@Component
public class BlogContentHandlerStrategyFactory {

	/** Index of strategies keyed by the handle type each one supports. */
	private final Map<ContentHandleType, BlogContentHandleStrategy> blogContentHandleStrategyMap;

	/**
	 * Builds the type-to-strategy index from all strategies discovered by Spring.
	 * @param strategies every {@link BlogContentHandleStrategy} bean in the context
	 */
	public BlogContentHandlerStrategyFactory(List<BlogContentHandleStrategy> strategies) {
		// Index each strategy by the ContentHandleType it reports.
		blogContentHandleStrategyMap = strategies.stream()
			.collect(Collectors.toMap(BlogContentHandleStrategy::getContentHandleType, Function.identity()));
	}

	/**
	 * Returns the strategy registered for the given handle type.
	 * @param type the content handle type to resolve
	 * @return the matching strategy
	 * @throws IllegalArgumentException if no strategy is registered for the type
	 */
	public BlogContentHandleStrategy getContentHandlerStrategy(ContentHandleType type) {
		// Fail fast for unsupported types rather than returning null.
		if (!blogContentHandleStrategyMap.containsKey(type))
			throw new IllegalArgumentException("Invalid content handler strategy type: " + type);

		return blogContentHandleStrategyMap.get(type);
	}

}
