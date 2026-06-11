package com.nullcanvas.blogapp.auth.oauth.handler;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.request.OAuthAuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Bridges Spring Security's OAuth2 login success into the application's own token
 * issuance.
 *
 * <p>
 * After a provider authenticates the user, this handler converts the provider session
 * into an application access token, sets it as a cookie, and redirects the browser to the
 * frontend's post-login page.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	/** Frontend base URL the browser is redirected to after login completes. */
	@Value("${app.frontend-url}")
	private String frontendUrl;

	/**
	 * Issues the application's own access token from the provider's authenticated user.
	 */
	private final AuthenticationService authenticationService;

	/**
	 * Invoked by Spring Security once a provider has successfully authenticated the user.
	 * @param request the inbound request
	 * @param response the response used to set the cookie and issue the redirect
	 * @param authentication the provider's authentication token holding the user profile
	 * @throws IOException if writing the redirect fails
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// Spring hands us an OAuth2-specific token; narrow to it to read provider
		// details.
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

		OAuth2User user = token.getPrincipal();
		log.debug("Oauth request has come");

		// The registration id ("google"/"github") tells us which provider just
		// authenticated.
		String authorizedClientRegistrationId = token.getAuthorizedClientRegistrationId();

		log.info("authorizedClientRegistrationId: {}", authorizedClientRegistrationId);

		// Wrap the provider user into our request type so the OAuth strategy can
		// find/create the account.
		AuthenticationRequest req = new OAuthAuthenticationRequest(user,
				ProviderType.fromString(authorizedClientRegistrationId));

		// Run our authentication pipeline to mint an application JWT for this user.
		AuthenticationResponse authenticationResponse = authenticationService.authenticate(req);

		// Deliver the token as an HttpOnly cookie valid for one hour.
		Cookie cookie = new Cookie("access_token", authenticationResponse.getToken());
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60);

		response.addCookie(cookie);

		// Send the browser back to the frontend, which reads the cookie to complete
		// sign-in.
		response.sendRedirect(frontendUrl + "/oauth-success");

	}

}
