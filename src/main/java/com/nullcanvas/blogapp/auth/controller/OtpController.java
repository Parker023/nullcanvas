package com.nullcanvas.blogapp.auth.controller;

import com.nullcanvas.blogapp.auth.dto.request.ResendOtpRequest;
import com.nullcanvas.blogapp.auth.service.UserRegistrationService;
import com.nullcanvas.blogapp.auth.service.VerifyEmailRequest;
import com.nullcanvas.blogapp.auth.service.otp.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Anirudh
 * @since 03/06/26
 */
@RestController
@RequestMapping("/auth/otp/v1")
@RequiredArgsConstructor
public class OtpController {
    private final UserRegistrationService registrationService;
    private final OtpService otpService;

    @PostMapping("/verify-otp")
    public ResponseEntity<Void> verify(
            @RequestBody @Valid VerifyEmailRequest request
    ) {

        registrationService.verifyEmail(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<Void> resendOtp(@RequestBody @Valid ResendOtpRequest request) {
        otpService.resendOtp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
