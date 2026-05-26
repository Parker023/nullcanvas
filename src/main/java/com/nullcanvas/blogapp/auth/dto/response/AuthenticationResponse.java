package com.nullcanvas.blogapp.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Anirudh
 * @since 25/05/26
 */

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class AuthenticationResponse {
    private String token;
    private UserResponse userResponse;
}
