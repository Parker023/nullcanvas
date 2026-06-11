package com.nullcanvas.blogapp.blog.exception;

/**
 * @author Anirudh
 * @since 11/06/26
 */
public class ContentParsingException extends RuntimeException {
    public ContentParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
