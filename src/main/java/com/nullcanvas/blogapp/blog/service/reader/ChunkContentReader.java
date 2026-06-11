package com.nullcanvas.blogapp.blog.service.reader;

import com.nullcanvas.blogapp.blog.dto.response.ChunkContentResponse;

import java.util.UUID;

public interface ChunkContentReader {

    ChunkContentResponse read(
            UUID blogId,
            Integer sequence);
}