package com.nullcanvas.blogapp.auth.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpCooldownService {

    @Value("${app.otp.rate-limit.cooldown}")
    private Duration cooldown;

    private static final String PREFIX =
            "auth:otp:cooldown:";

    private final StringRedisTemplate redisTemplate;

    public boolean canResend(String email) {

        String key = PREFIX + email;

        Boolean exists =
                redisTemplate.hasKey(key);

        if (Boolean.TRUE.equals(exists)) {
            return false;
        }

        redisTemplate.opsForValue()
                .set(
                        key,
                        "1",
                        cooldown
                );

        return true;
    }
}