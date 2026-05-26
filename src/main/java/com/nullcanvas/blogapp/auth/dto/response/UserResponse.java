package com.nullcanvas.blogapp.auth.dto.response;

import com.nullcanvas.blogapp.auth.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 26/05/26
 */

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class UserResponse {

    private UUID publicId;
    private String name;
    private String email;
    private Role role;
    private String profileImageUrl;
}
