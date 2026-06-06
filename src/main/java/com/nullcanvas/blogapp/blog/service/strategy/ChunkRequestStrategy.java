package com.nullcanvas.blogapp.blog.service.strategy;

import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import com.nullcanvas.blogapp.blog.dto.request.BlogContentRequest;
import com.nullcanvas.blogapp.blog.dto.request.ChunkContentRequest;
import com.nullcanvas.blogapp.blog.dto.request.FullContentRequest;
import com.nullcanvas.blogapp.blog.exception.InvalidRequestTypeException;
import com.nullcanvas.blogapp.blog.model.ChunkStore;
import com.nullcanvas.blogapp.blog.repository.BlogRepository;
import com.nullcanvas.blogapp.blog.repository.ChunkStoreRepository;
import com.nullcanvas.blogapp.blog.service.BlogMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final FullRequestStrategy fullRequestStrategy;

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
            JsonNode assembledContent = assembleChunks(chunkStores);

            log.info("{} chunks for {} aggregated", chunkStores.size(), blogPublicId);
            FullContentRequest fullContentRequest = FullContentRequest.builder()
                    .content(assembledContent)
                    .build();
            fullRequestStrategy.handle(fullContentRequest, blogPublicId);

            cleanUpChunkStores(blogPublicId);

        }
    }

    private void cleanUpChunkStores(UUID blogPublicId) {
        chunkStoreRepository.deleteByBlogPublicId(blogPublicId);
    }

    private ChunkStore prepareChunkStore(ChunkContentRequest contentRequest, UUID blogPublicId) {
        return ChunkStore.builder()
                .blogPublicId(blogPublicId)
                .isFinalChunk(contentRequest.isFinalChunk())
                .chunkSeq(contentRequest.chunkSeq())
                .chunk(contentRequest.chunk())
                .build();
    }

    private JsonNode assembleChunks(List<ChunkStore> chunks) {

        String content = chunks.stream()
                .map(ChunkStore::getChunk)
                .collect(Collectors.joining());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode contentNode =
                mapper.createObjectNode();

        contentNode.put(
                "content",
                content
        );
        return contentNode;
    }
}
