package com.nullcanvas.blogapp.auth.strategy;

import com.nullcanvas.blogapp.auth.dto.AuthType;
import com.nullcanvas.blogapp.auth.dto.Role;
import com.nullcanvas.blogapp.auth.dto.request.AuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.request.OAuthAuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.response.UserResponse;
import com.nullcanvas.blogapp.auth.jwt.JwtService;
import com.nullcanvas.blogapp.auth.model.User;
import com.nullcanvas.blogapp.auth.oauth.adapter.OAuthUserAdapter;
import com.nullcanvas.blogapp.auth.oauth.dto.OAuthUser;
import com.nullcanvas.blogapp.auth.oauth.factory.OAuthUserAdapterFactory;
import com.nullcanvas.blogapp.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Anirudh
 * @since 25/05/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthAuthenticationStrategy implements AuthenticationStrategy {

    private final OAuthUserAdapterFactory oAuthUserAdapterFactory;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        log.debug("Inside OAuth strategy");

        OAuthAuthenticationRequest oAuthAuthenticationRequest = (OAuthAuthenticationRequest) request;

        OAuthUserAdapter adapter = oAuthUserAdapterFactory.getAdapter(oAuthAuthenticationRequest.getProvider());
        log.debug("Adapter selected: {}", adapter.getProviderType());

        OAuthUser oAuthUser = adapter.adapt(oAuthAuthenticationRequest.getOauth2User());

        Optional<User> optionalUser = userRepository.findByEmail(oAuthUser.getEmail());


        if (optionalUser.isEmpty()) {
            User translatedUser = translate(oAuthUser);
            User savedUser = userRepository.save(translatedUser);
            optionalUser = Optional.of(savedUser);
        }

        User fetchedUser = optionalUser.get();

        String token = jwtService.generateToken(fetchedUser);

        return prepareResponse(token, fetchedUser);
    }

    @Override
    public AuthType getType() {
        return AuthType.OAUTH2;
    }

    private User translate(OAuthUser oauthUser) {
        return User
                .builder()
                .email(oauthUser.getEmail())
                .name(oauthUser.getName())
                .profileImageUrl(oauthUser.getProfileImageUrl())
                .providerType(oauthUser.getProviderType())
                .providerUserId(oauthUser.getProviderUserId())
                .role(Role.USER)
                .emailVerified(true)
                .password(null)
                .build();
    }

    private AuthenticationResponse prepareResponse(String token, User user) {
        UserResponse userResponse = UserResponse
                .builder()
                .email(user.getEmail())
                .publicId(user.getPublicId())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .name(user.getName())
                .build();

        return AuthenticationResponse.builder()
                .userResponse(userResponse)
                .token(token)
                .build();

    }


}
