package com.nullcanvas.blogapp.auth.service;

import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import com.nullcanvas.blogapp.auth.strategy.AuthenticationStrategy;
import com.nullcanvas.blogapp.factory.AuthStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Anirudh
 * @since 26/05/26
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthStrategyFactory strategyFactory;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        AuthenticationStrategy strategy = strategyFactory.getStrategy(authenticationRequest.getAuthType());
        return strategy.authenticate(authenticationRequest);

    }
}
