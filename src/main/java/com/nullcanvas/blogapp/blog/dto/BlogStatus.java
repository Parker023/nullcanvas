package com.nullcanvas.blogapp.blog.dto;

/**
 * @author Anirudh
 * @since 01/06/26
 */
public enum BlogStatus {

    DRAFT, ARCHIVED, PUBLISHED;

    public static BlogStatus fromString(String value) {
        return switch (value.toLowerCase()) {
            case "draft" -> DRAFT;
            case "archived" -> ARCHIVED;
            case "published" -> PUBLISHED;
            default -> throw new IllegalArgumentException("invalid status: " + value);
        };
    }
}
