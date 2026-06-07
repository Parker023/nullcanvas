package com.nullcanvas.blogapp.auth.security.configuration;

import com.nullcanvas.blogapp.auth.oauth.handler.OAuth2SuccessHandler;
import com.nullcanvas.blogapp.auth.security.entrypoint.JwtAuthenticationEntryPoint;
import com.nullcanvas.blogapp.auth.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central Spring Security configuration that wires the filter chain for the application.
 *
 * <p>
 * The API is stateless and JWT-based: public auth/OAuth endpoints are open, everything
 * else requires a valid token, sessions are disabled, CSRF is turned off (no cookies
 * drive server-side state beyond the token), and a custom JWT filter authenticates each
 * request.
 * </p>
 *
 * @author Anirudh
 * @since 24/05/26
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	/** Issues the app token and redirects after a successful OAuth2 login. */
	private final OAuth2SuccessHandler successHandler;

	/**
	 * Produces the 401 response when an unauthenticated request hits a protected
	 * endpoint.
	 */
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;

	/** Reads and validates the JWT on each request, populating the security context. */
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	/**
	 * Builds the security filter chain defining authorization rules and authentication
	 * mechanisms.
	 * @param http the Spring Security builder
	 * @return the configured filter chain
	 * @throws Exception if the chain cannot be built
	 */
	@Bean
	public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {

		return http
			// Allow CORS preflight and the public auth/oauth/health/error endpoints;
			// secure the rest.
			.authorizeHttpRequests(req -> req.requestMatchers(HttpMethod.OPTIONS, "/**")
				.permitAll()
				.requestMatchers("/auth/**", "/oauth2/**", "/login/oauth2/**", "/actuator/health", "/error")
				.permitAll()
				.anyRequest()
				.authenticated())
			// Apply the CORS configuration defined in CorsConfig.
			.cors(Customizer.withDefaults())
			// Route OAuth2 logins through our success handler so it mints an app token.
			.oauth2Login(oauth2 -> oauth2.successHandler(successHandler))
			// CSRF protection is unnecessary for a stateless, token-authenticated API.
			.csrf(CsrfConfigurer::disable)
			// No server-side sessions: each request must carry its own JWT.
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// Return a JSON 401 (instead of a redirect) when authentication is missing.
			.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
			// Authenticate via our JWT filter before the username/password filter runs.
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

}