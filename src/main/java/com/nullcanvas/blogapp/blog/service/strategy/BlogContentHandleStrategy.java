package com.nullcanvas.blogapp.blog.service.strategy;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;

/**
 * @author Anirudh
 * @since 02/06/26
 */
public interface BlogContentHandleStrategy {

    ContentHandleType getContentHandleType();

    void handleInsert();

}
