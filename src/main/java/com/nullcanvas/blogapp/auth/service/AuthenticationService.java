package com.nullcanvas.blogapp.auth.service;

import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import com.nullcanvas.blogapp.auth.strategy.AuthenticationStrategy;
import com.nullcanvas.blogapp.auth.factory.AuthStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Anirudh
 * @since 26/05/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthStrategyFactory strategyFactory;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        AuthenticationStrategy strategy = strategyFactory.getStrategy(authenticationRequest.getAuthType());
        log.debug("Strategy selected: {}", strategy.getType());
        return strategy.authenticate(authenticationRequest);

    }
}
