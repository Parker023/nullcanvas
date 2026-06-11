package com.nullcanvas.blogapp.auth.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis configuration for the auth module.
 *
 * <p>
 * Redis backs the OTP storage and the rate-limit/cooldown counters, where short-lived
 * string values with TTLs are a natural fit.
 * </p>
 *
 * @author Anirudh
 * @since 31/05/26
 */
@Configuration
public class RedisConfig {

	/**
	 * Provides a string-typed Redis template for the OTP and rate-limit services.
	 * @param connectionFactory the Redis connection factory auto-configured by Spring
	 * @return a template that reads/writes string keys and values
	 */
	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

}
