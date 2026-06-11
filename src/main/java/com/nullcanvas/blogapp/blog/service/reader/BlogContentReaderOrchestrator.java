package com.nullcanvas.blogapp.blog.service.reader;

import com.nullcanvas.blogapp.blog.dto.response.ChunkContentResponse;
import com.nullcanvas.blogapp.blog.dto.response.FullContentResponse;

import java.util.UUID;

public interface BlogContentReaderOrchestrator {

	FullContentResponse getFullContent(UUID blogId);

	ChunkContentResponse getChunk(UUID blogId, Integer sequence);

}