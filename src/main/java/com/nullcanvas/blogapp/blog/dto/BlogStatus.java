package com.nullcanvas.blogapp.blog.dto;

/**
 * @author Anirudh
 * @since 01/06/26
 */
public enum BlogStatus {

    DRAFT, ARCHIVED, PUBLISHED, IN_PROGRESS;

    public static BlogStatus fromString(String value) {
        return switch (value.toLowerCase()) {
            case "draft" -> DRAFT;
            case "archived" -> ARCHIVED;
            case "published" -> PUBLISHED;
            case "in_progress" -> IN_PROGRESS;
            default -> throw new IllegalArgumentException("invalid status: " + value);
        };
    }
}
