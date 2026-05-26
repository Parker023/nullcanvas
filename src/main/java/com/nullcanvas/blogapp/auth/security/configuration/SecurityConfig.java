package com.nullcanvas.blogapp.auth.security.configuration;

import com.nullcanvas.blogapp.auth.oauth.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author Anirudh
 * @since 24/05/26
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler successHandler;


    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(req -> req
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/test",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/actuator/health"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(successHandler)
                )
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .build();
    }

}