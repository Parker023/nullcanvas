package com.nullcanvas.blogapp.auth.controller;

import com.nullcanvas.blogapp.auth.dto.request.JwtAuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.request.LoginRequest;
import com.nullcanvas.blogapp.auth.dto.request.RegisterRequest;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.service.AuthenticationService;
import com.nullcanvas.blogapp.auth.service.UserRegistrationService;
import com.nullcanvas.blogapp.auth.service.VerifyEmailRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

import static com.nullcanvas.blogapp.auth.constants.AuthConstants.ACCESS_TOKEN;

/**
 * @author Anirudh
 * @since 17/05/26
 */

@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserRegistrationService registrationService;

    @GetMapping("/test")
    public String test() {
        return "null canvas App Up and Running";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

        JwtAuthenticationRequest authRequest =
                new JwtAuthenticationRequest(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                );

        AuthenticationResponse authResponse =
                authenticationService.authenticate(authRequest);

        ResponseCookie cookie =
                ResponseCookie.from(ACCESS_TOKEN, authResponse.getToken())
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .sameSite("Lax")
                        .maxAge(Duration.ofDays(1))
                        .build();


        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        registrationService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Void> verify(
            @RequestBody VerifyEmailRequest request
    ) {

        registrationService.verifyEmail(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletResponse response
    ) {


        ResponseCookie cookie =
                ResponseCookie.from(ACCESS_TOKEN, null)
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(0)
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
        return ResponseEntity.ok().build();
    }

}
