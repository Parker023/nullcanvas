package com.nullcanvas.blogapp.auth.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpAttemptService {

    @Value("${app.otp.rate-limit.attempt.retries}")
    private int maxAttempts;

    @Value("${app.otp.rate-limit.attempt.window}")
    private Duration window;

    private static final String PREFIX =
            "auth:otp:attempts:";

    private final StringRedisTemplate redisTemplate;

    public boolean recordFailure(String email) {

        String key = PREFIX + email;

        Long count =
                redisTemplate.opsForValue()
                        .increment(key);

        if (count == 1) {

            redisTemplate.expire(
                    key,
                    window
            );
        }

        return count <= maxAttempts;
    }

    public void reset(String email) {

        redisTemplate.delete(PREFIX + email);
    }
}