package com.nullcanvas.blogapp.auth.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Cross-Origin Resource Sharing (CORS) configuration for browser clients.
 *
 * <p>
 * Allows the trusted frontend origins to call the API and, crucially, to send credentials
 * so the {@code HttpOnly} access-token cookie is included on cross-origin requests.
 * </p>
 *
 * @author Anirudh
 * @since 30/05/26
 */
@Configuration
public class CorsConfig {

	/**
	 * Defines which origins, methods and headers are permitted for cross-origin requests.
	 * @return the CORS source applied to all paths
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();

		// Only the local dev frontend and the production domains may call the API.
		configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://nullcanvas.blog",
				"https://nullcanvas.blog", "https://app.nullcanvas.blog"));

		// Permit the standard verbs plus OPTIONS for CORS preflight.
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		configuration.setAllowedHeaders(List.of("*"));

		// Required so the browser sends the auth cookie on cross-origin calls.
		configuration.setAllowCredentials(true);

		// Apply this configuration to every endpoint.
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

}
