package com.nullcanvas.blogapp.auth.service;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import com.nullcanvas.blogapp.auth.dto.request.RegisterRequest;
import com.nullcanvas.blogapp.auth.dto.Role;
import com.nullcanvas.blogapp.auth.exception.InvalidOtpException;
import com.nullcanvas.blogapp.auth.exception.UserAlreadyExistsException;
import com.nullcanvas.blogapp.auth.model.User;
import com.nullcanvas.blogapp.auth.repository.UserRepository;
import com.nullcanvas.blogapp.auth.service.otp.OtpService;
import com.nullcanvas.blogapp.auth.util.AuthenticationAppUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Anirudh
 * @since 28/05/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final AuthenticationAppUtil appUtil;
    private final OtpService otpService;

    @Transactional
    public void register(RegisterRequest request) {
        log.debug("inside Registration service");
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException();
        }
        User user = translate(request);


        userRepository.save(user);
        log.info("user {} is registered with null canvas", request.email());

        otpService.sendVerificationOtp(request.email());

    }

    public void verifyEmail(
            VerifyEmailRequest request
    ) {


        boolean valid =
                otpService.validateOtp(
                        request.email(),
                        request.otp()
                );

        if (!valid) {
            throw new InvalidOtpException("OTP is not Valid/ TTL Expired");
        }

        User user =
                userRepository.findByEmail(
                        request.email()
                ).orElseThrow();

        if (user.isEmailVerified()) {
            log.info("{} email already verified", request.email());
            return;
        }


        user.setEmailVerified(true);

        userRepository.save(user);

        otpService.deleteOtp(request.email());
    }

    private User translate(RegisterRequest request) {
        return User.builder()
                .password(appUtil.hashString(request.password()))
                .role(Role.USER)
                .emailVerified(false)
                .name(request.name())
                .email(request.email())
                .providerType(ProviderType.LOCAL)
                .profileImageUrl(null)
                .providerUserId("0")
                .build();
    }
}
