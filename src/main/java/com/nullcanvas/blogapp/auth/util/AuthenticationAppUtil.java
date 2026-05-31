package com.nullcanvas.blogapp.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * @author Anirudh
 * @since 28/05/26
 */
@Component
@RequiredArgsConstructor
public class AuthenticationAppUtil {

    private final PasswordEncoder passwordEncoder;

    public String hashString(String value) {
        return passwordEncoder.encode(value);
    }


    public boolean matches(String rawValue, String encodedValue) {
        return passwordEncoder.matches(rawValue, encodedValue);
    }
}
