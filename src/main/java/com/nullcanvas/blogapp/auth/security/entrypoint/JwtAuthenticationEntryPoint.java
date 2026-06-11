package com.nullcanvas.blogapp.auth.security.entrypoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

/**
 * Entry point that returns a JSON {@code 401} when an unauthenticated request hits a
 * secured route.
 *
 * <p>
 * Because the API is stateless, the default behaviour of redirecting to a login page is
 * undesirable; this writes a structured error body instead so SPA/API clients can handle
 * it programmatically.
 * </p>
 *
 * @author Anirudh
 * @since 28/05/26
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/** Serialises the error body to JSON. */
	private final ObjectMapper objectMapper;

	/**
	 * Writes a JSON 401 response describing the authentication failure.
	 * @param request the request that failed authentication
	 * @param response the response the error body is written to
	 * @param authException the authentication failure that triggered this entry point
	 * @throws IOException if writing to the response stream fails
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		// Build a structured error payload including the failing path and reason.
		Map<String, Object> body = Map.of("timestamp", Instant.now(), "status", 401, "error", "Unauthorized", "message",
				authException.getMessage(), "path", request.getRequestURI());
		// Serialise the map straight to the response stream as JSON.
		objectMapper.writeValue(response.getOutputStream(), body);
	}

}
