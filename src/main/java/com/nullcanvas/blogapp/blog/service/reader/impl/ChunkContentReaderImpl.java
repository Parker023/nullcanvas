package com.nullcanvas.blogapp.blog.service.reader.impl;

import com.nullcanvas.blogapp.blog.dto.response.ChunkContentResponse;
import com.nullcanvas.blogapp.blog.service.reader.ChunkContentReader;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 11/06/26
 */
@Service
public class ChunkContentReaderImpl implements ChunkContentReader {
    @Override
    public ChunkContentResponse read(UUID blogId, Integer sequence) {
        return null;
    }
}
