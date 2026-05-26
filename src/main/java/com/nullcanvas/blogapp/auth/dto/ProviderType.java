package com.nullcanvas.blogapp.auth.dto;

/**
 * @author Anirudh
 * @since 26/05/26
 */
public enum ProviderType {
    GOOGLE, GITHUB;

    public static ProviderType fromString(String type) {
        return switch (type.toLowerCase()) {
            case "google" -> GOOGLE;
            case "github" -> GITHUB;
            default -> throw new IllegalArgumentException(type + " does not support");
        };
    }
}
