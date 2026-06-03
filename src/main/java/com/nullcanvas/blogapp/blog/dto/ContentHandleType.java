package com.nullcanvas.blogapp.blog.dto;

/**
 * @author Anirudh
 * @since 01/06/26
 */
public enum ContentHandleType {

    FULL, CHUNK;

    public static ContentHandleType fromString(String value) {
        return switch (value.toLowerCase()) {
            case "full" -> FULL;
            case "chunk" -> CHUNK;
            default -> throw new IllegalArgumentException("Invalid chunk handle type: " + value);
        };

    }

}
