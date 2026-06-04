package com.nullcanvas.blogapp.blog.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 01/06/26
 */
@Builder
@Getter
@AllArgsConstructor
public class ChunkRequest {

    private UUID blogPublicId;

    private long chunkSeq;

    private String chunk;

    private boolean isFinalChunk;
}
