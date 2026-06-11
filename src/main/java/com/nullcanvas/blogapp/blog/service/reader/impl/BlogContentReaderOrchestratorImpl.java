package com.nullcanvas.blogapp.blog.service.reader.impl;

import com.nullcanvas.blogapp.blog.dto.response.ChunkContentResponse;
import com.nullcanvas.blogapp.blog.dto.response.FullContentResponse;
import com.nullcanvas.blogapp.blog.service.reader.BlogContentReaderOrchestrator;
import com.nullcanvas.blogapp.blog.service.reader.ChunkContentReader;
import com.nullcanvas.blogapp.blog.service.reader.FullContentReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogContentReaderOrchestratorImpl implements BlogContentReaderOrchestrator {

	private final FullContentReader fullReader;

	private final ChunkContentReader chunkReader;

	@Override
	public FullContentResponse getFullContent(UUID blogId) {
		return fullReader.read(blogId);
	}

	@Override
	public ChunkContentResponse getChunk(UUID blogId, Integer sequence) {

		return chunkReader.read(blogId, sequence);
	}

}