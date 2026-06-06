package com.nullcanvas.blogapp.auth.strategy;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.dto.request.JwtAuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.response.UserResponse;
import com.nullcanvas.blogapp.auth.exception.InvalidCredentialsException;
import com.nullcanvas.blogapp.auth.exception.UserNotRegisteredException;
import com.nullcanvas.blogapp.auth.jwt.JwtService;
import com.nullcanvas.blogapp.auth.model.User;
import com.nullcanvas.blogapp.auth.repository.UserRepository;
import com.nullcanvas.blogapp.auth.util.AuthenticationAppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Anirudh
 * @since 25/05/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationStrategy implements AuthenticationStrategy {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationAppUtil appUtil;


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        JwtAuthenticationRequest jwtAuthenticationRequest = (JwtAuthenticationRequest) request;

        User user = userRepository
                .findByEmail(jwtAuthenticationRequest
                        .email())
                .orElseThrow(UserNotRegisteredException::new);

        if (Objects.isNull(user.getPassword()))
            throw new UserNotRegisteredException();

        boolean passwordMatch = appUtil.matches(jwtAuthenticationRequest.password(), user.getPassword());

        if (!passwordMatch) {
            throw new InvalidCredentialsException();
        }

        log.info("{} user is authenticated", user.getEmail());

        String token = jwtService.generateToken(user);


        return prepareResponse(token, user);
    }

    @Override
    public AuthType getType() {
        return AuthType.JWT;
    }

    private AuthenticationResponse prepareResponse(String token, User user) {
        UserResponse userResponse = UserResponse.builder()
                .name(user.getName())
                .roles(List.of(user.getRole()))
                .profileImageUrl(user.getProfileImageUrl())
                .publicId(user.getPublicId())
                .email(user.getEmail())
                .build();


        return AuthenticationResponse.builder()
                .userResponse(userResponse)
                .token(token)
                .build();
    }
}
