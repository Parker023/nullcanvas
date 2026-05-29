package com.nullcanvas.blogapp.auth.service.otp;

import com.nullcanvas.blogapp.auth.exception.CoolDownException;
import com.nullcanvas.blogapp.auth.exception.TooManyFailedOtpVerificationException;
import com.nullcanvas.blogapp.auth.exception.TooManyOtpRequestException;
import com.nullcanvas.blogapp.auth.service.EmailService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Anirudh
 * @since 28/05/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    @Value("${app.otp.otp-ttl}")
    private Duration ttl;

    @Value("${spring.data.redis.host:NOT_SET}")
    private String redisHost;

    @PostConstruct
    public void init() {
        log.info("Redis host = {}", redisHost);
    }

    private static final String PREFIX = "auth:email-verification:";

    private final StringRedisTemplate redisTemplate;
    private final OtpCooldownService otpCooldownService;
    private final OtpRateLimitService otpRateLimitService;
    private final OtpAttemptService otpAttemptService;
    private final EmailService emailService;

    public void sendVerificationOtp(
            String email
    ) throws CoolDownException {

        validateOtpRequest(email);

        String otp = generateOtp();

        redisTemplate.opsForValue().set(
                PREFIX + email,
                otp,
                ttl
        );

        emailService.sendVerificationOtp(
                email,
                otp
        );

        log.info(
                "Verification OTP sent to {}",
                email
        );


    }

    public boolean validateOtp(
            String email,
            String otp
    ) {

        String storedOtp =
                redisTemplate.opsForValue()
                        .get(PREFIX + email);

        if (storedOtp == null) {
            return false;
        }

        boolean valid = storedOtp.equals(otp);

        if (!valid) {

            boolean allowed =
                    otpAttemptService.recordFailure(email);

            if (!allowed) {
                throw new TooManyFailedOtpVerificationException();
            }

            return false;
        }

        otpAttemptService.reset(email);

        return true;
    }

    public void deleteOtp(String email) {

        redisTemplate.delete(PREFIX + email);
    }


    public String generateOtp() {

        return String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100000, 1000000)
        );
    }

    private void validateOtpRequest(
            String email
    ) {

        if (!otpCooldownService.canResend(email)) {
            throw new CoolDownException();
        }

        if (!otpRateLimitService.isAllowed(email)) {
            throw new TooManyOtpRequestException();
        }
    }
}
