package com.nullcanvas.blogapp.auth.service.otp;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpRateLimitService {

    @Value("${app.otp.rate-limit.max-req}")
    private long maxRequests;

    @Value("${app.otp.rate-limit.window}")
    private Duration window;

    private static final String PREFIX =
            "auth:otp:rate-limit:";

    private final StringRedisTemplate redisTemplate;

    public boolean isAllowed(String email) {

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

        return count <= maxRequests;
    }
}