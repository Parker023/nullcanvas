package com.nullcanvas.blogapp.auth.security.filter;

import com.nullcanvas.blogapp.auth.jwt.JwtService;
import com.nullcanvas.blogapp.auth.security.principal.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.nullcanvas.blogapp.auth.constants.AuthConstants.ACCESS_TOKEN;
import static com.nullcanvas.blogapp.auth.constants.AuthConstants.EMAIL;
import static com.nullcanvas.blogapp.auth.constants.AuthConstants.ROLE;

/**
 * Per-request filter that authenticates the caller from the JWT stored in the
 * access-token cookie.
 *
 * <p>
 * Extends {@link OncePerRequestFilter} so it runs at most once per request. When a valid
 * token is present it reconstructs the user's authentication and places it in the
 * security context; an invalid token clears the context, leaving the request anonymous.
 * </p>
 *
 * @author Anirudh
 * @since 28/05/26
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	/** Validates the token and extracts its claims. */
	private final JwtService jwtService;

	/**
	 * Reads the JWT cookie, validates it, and populates the security context for the
	 * request.
	 * @param request the inbound request
	 * @param response the outbound response
	 * @param filterChain the remaining filter chain to continue processing
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Pull the token out of the access-token cookie, if present.
		String accessTokenFromCookie = getAccessTokenFromCookie(request.getCookies());

		log.info("access token: {}", accessTokenFromCookie);

		// No token means an anonymous request; let it proceed and be rejected later if
		// the route is secured.
		if (StringUtils.isEmpty(accessTokenFromCookie)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {

			// Only authenticate if the context isn't already populated (avoids redundant
			// work).
			if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
				// Verifying the claims also validates the signature and expiry.
				Claims claims = jwtService.extractClaims(accessTokenFromCookie);
				UUID userId = UUID.fromString(claims.getSubject());
				String email = claims.get(EMAIL, String.class);
				String role = claims.get(ROLE, String.class);
				// Rebuild Spring's authentication token from the trusted claims.
				UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(userId,
						email, role);

				// Mark the request as authenticated for the rest of the chain.
				SecurityContextHolder.getContext().setAuthentication(authentication);

			}
		}
		catch (Exception ex) {
			// Any failure (invalid/expired/tampered token) leaves the request
			// unauthenticated.
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);

	}

	/**
	 * Skips this filter for CORS preflight (OPTIONS) requests, which carry no
	 * credentials.
	 * @param request the request under consideration
	 * @return {@code true} for OPTIONS requests so the filter is bypassed
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {

		return HttpMethod.OPTIONS.matches(request.getMethod());
	}

	/**
	 * Builds a Spring authentication token from trusted JWT claims.
	 * @param userId the user's public id from the token subject
	 * @param email the user's email claim
	 * @param role the user's role claim
	 * @return an authenticated token wrapping a {@link CustomUserPrincipal}
	 */
	private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(UUID userId, String email,
			String role) {
		// Password is null because the user is already proven via the signed token, not
		// credentials.
		CustomUserPrincipal principal = new CustomUserPrincipal(userId, email, null,
				List.of(new SimpleGrantedAuthority(role)));

		return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
	}

	/**
	 * Extracts the access-token value from the request cookies.
	 * @param cookies the request's cookies, possibly {@code null}
	 * @return the token value, or {@code null} if the cookie is absent
	 */
	private String getAccessTokenFromCookie(Cookie[] cookies) {

		if (cookies == null) {
			return null;
		}

		// Scan the cookies for the one holding the access token.
		for (Cookie cookie : cookies) {

			if (ACCESS_TOKEN.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}

}
