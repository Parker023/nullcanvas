package com.nullcanvas.blogapp.blog.service.factory;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import com.nullcanvas.blogapp.blog.service.strategy.BlogContentHandleStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Anirudh
 * @since 04/06/26
 */
@Component
public class BlogContentHandlerStrategyFactory {
    private final Map<ContentHandleType, BlogContentHandleStrategy> blogContentHandleStrategyMap;

    public BlogContentHandlerStrategyFactory(List<BlogContentHandleStrategy> strategies) {
        blogContentHandleStrategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        BlogContentHandleStrategy::getContentHandleType,
                        Function.identity()
                ));
    }

    public BlogContentHandleStrategy getContentHandlerStrategy(ContentHandleType type) {
        if (!blogContentHandleStrategyMap.containsKey(type))
            throw new IllegalArgumentException("Invalid content handler strategy type: " + type);

        return blogContentHandleStrategyMap.get(type);
    }
}
