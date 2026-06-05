package com.nullcanvas.blogapp.blog.service.strategy;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import com.nullcanvas.blogapp.blog.dto.request.BlogContentRequest;
import com.nullcanvas.blogapp.blog.dto.request.ChunkContentRequest;
import com.nullcanvas.blogapp.blog.exception.BlogNotFoundException;
import com.nullcanvas.blogapp.blog.exception.InvalidRequestTypeException;
import com.nullcanvas.blogapp.blog.model.Blog;
import com.nullcanvas.blogapp.blog.model.ChunkStore;
import com.nullcanvas.blogapp.blog.repository.BlogRepository;
import com.nullcanvas.blogapp.blog.repository.ChunkStoreRepository;
import com.nullcanvas.blogapp.blog.service.BlogMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 02/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChunkRequestStrategy implements BlogContentHandleStrategy {
    private final ChunkStoreRepository chunkStoreRepository;
    private final BlogMetadataService blogMetadataService;
    private final BlogRepository blogRepository;

    @Override
    public ContentHandleType getContentHandleType() {
        return ContentHandleType.CHUNK;
    }

    @Transactional
    @Override
    public void handle(BlogContentRequest contentRequest, UUID blogPublicId) {

        if (!(contentRequest instanceof ChunkContentRequest(long chunkSeq, String chunk, boolean isFinalChunk)))
            throw new InvalidRequestTypeException();

        ChunkContentRequest request = (ChunkContentRequest) contentRequest;

        ChunkStore chunkStore = prepareChunkStore(request, blogPublicId);

        ChunkStore savedChunk = chunkStoreRepository.saveAndFlush(chunkStore);

        if (savedChunk.isFinalChunk()) {
            List<ChunkStore> chunkStores = chunkStoreRepository.findAllByBlogPublicId(blogPublicId);
            String assembledContent = assembleChunks(chunkStores);

            Blog blog = blogRepository.findByBlogPublicId(blogPublicId)
                    .orElseThrow(BlogNotFoundException::new);

            Blog updatedBlog = blog.toBuilder()
                    .contentJson(assembledContent)
                    .contentSize((long) StringUtils.length(assembledContent))
                    .build();

            blogRepository.save(updatedBlog);

        }
    }

    private ChunkStore prepareChunkStore(ChunkContentRequest contentRequest, UUID blogPublicId) {
        return ChunkStore.builder()
                .blogPublicId(blogPublicId)
                .isFinalChunk(contentRequest.isFinalChunk())
                .chunkSeq(contentRequest.chunkSeq())
                .build();
    }

    private String assembleChunks(List<ChunkStore> chunks) {

        StringBuilder contentHolder = new StringBuilder();
        chunks.stream()
                .map(ChunkStore::getChunk)
                .forEach(contentHolder::append);
        return contentHolder.toString();
    }
}
