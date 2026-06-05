package com.nullcanvas.blogapp.blog.dto.request;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import lombok.Builder;


/**
 * @author Anirudh
 * @since 01/06/26
 */
@Builder

public record ChunkContentRequest(long chunkSeq,String chunk,boolean isFinalChunk) implements BlogContentRequest {
    @Override
    public ContentHandleType getContentHandleType() {
        return ContentHandleType.CHUNK;
    }
}
