package com.nullcanvas.blogapp.blog.dto.request;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import lombok.Builder;

/**
 * Content upload for a single ordered chunk of a blog body.
 *
 * @param chunkSeq the chunk's position in the sequence, used to reassemble the body in
 * order
 * @param chunk the raw content of this chunk
 * @param isFinalChunk whether this is the last chunk, signalling the upload is complete
 * @author Anirudh
 * @since 01/06/26
 */
@Builder

public record ChunkContentRequest(long chunkSeq, String chunk, boolean isFinalChunk) implements BlogContentRequest {
	/**
	 * @return always {@link ContentHandleType#CHUNK}, routing this to the chunk strategy
	 */
	@Override
	public ContentHandleType getContentHandleType() {
		return ContentHandleType.CHUNK;
	}
}
