package com.nullcanvas.blogapp.auth.oauth.handler;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.request.OAuthAuthenticationRequest;
import com.nullcanvas.blogapp.auth.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationService authenticationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        OAuth2User user = token.getPrincipal();

        String authorizedClientRegistrationId = token.getAuthorizedClientRegistrationId();

        AuthenticationRequest req = new OAuthAuthenticationRequest(user, ProviderType.fromString(authorizedClientRegistrationId));

        authenticationService.authenticate(req);

    }
}
