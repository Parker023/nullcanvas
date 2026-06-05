package com.nullcanvas.blogapp.blog.dto.request;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import lombok.Builder;


/**
 * @author Anirudh
 * @since 04/06/26
 */
@Builder
public record FullContentRequest(String content) implements BlogContentRequest {
    @Override
    public ContentHandleType getContentHandleType() {
        return ContentHandleType.FULL;
    }
}
