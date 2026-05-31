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
 * @author Anirudh
 * @since 26/05/26
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final AuthenticationService authenticationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        OAuth2User user = token.getPrincipal();
        log.debug("Oauth request has come");

        String authorizedClientRegistrationId = token.getAuthorizedClientRegistrationId();

        log.info("authorizedClientRegistrationId: {}", authorizedClientRegistrationId);

        AuthenticationRequest req = new OAuthAuthenticationRequest(user, ProviderType.fromString(authorizedClientRegistrationId));

        AuthenticationResponse authenticationResponse = authenticationService.authenticate(req);

        Cookie cookie = new Cookie("access_token", authenticationResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);

        response.addCookie(cookie);

        response.sendRedirect(frontendUrl+"/oauth-success");


    }
}
