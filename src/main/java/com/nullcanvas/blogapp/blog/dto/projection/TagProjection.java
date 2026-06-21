package com.nullcanvas.blogapp.blog.dto.projection;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 19/06/26
 */
public interface TagProjection {
    String getTagName();

    UUID getPublicId();
}
