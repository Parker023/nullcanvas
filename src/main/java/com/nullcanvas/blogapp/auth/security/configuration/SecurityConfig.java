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
 * @author Anirudh
 * @since 24/05/26
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final OAuth2SuccessHandler successHandler;

	private final JwtAuthenticationEntryPoint authenticationEntryPoint;

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {

		return http
			.authorizeHttpRequests(req -> req.requestMatchers(HttpMethod.OPTIONS, "/**")
				.permitAll()
				.requestMatchers("/auth/**", "/oauth2/**", "/login/oauth2/**", "/actuator/health", "/error")
				.permitAll()
				.anyRequest()
				.authenticated())
			.cors(Customizer.withDefaults())
			.oauth2Login(oauth2 -> oauth2.successHandler(successHandler))
			.csrf(CsrfConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

}