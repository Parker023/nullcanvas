package com.nullcanvas.blogapp.auth.oauth.dto;

import com.nullcanvas.blogapp.auth.dto.ProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Anirudh
 * @since 26/05/26
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUser {

    private String providerUserId;

    private String email;

    private String name;

    private String profileImageUrl;

    private ProviderType providerType;
}