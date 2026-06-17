package com.nullcanvas.blogapp.blog.service.writer.strategy;

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
 * Content strategy that persists in a blog's body as incremental chunks.
 *
 * <p>
 * Each chunk is stored as it arrives. When the final chunk is received, all chunks are
 * read back in order, concatenated into the complete body, handed to
 * {@link FullRequestStrategy} to persist on the blog, and the now-redundant chunk rows
 * are deleted.
 * </p>
 *
 * @author Anirudh
 * @since 02/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChunkRequestStrategy implements BlogContentHandleStrategy {

	/**
	 * Persistence for individual chunks.
	 */
	private final ChunkStoreRepository chunkStoreRepository;

	/**
	 * Blog metadata service (available for related metadata operations).
	 */
	private final BlogMetadataService blogMetadataService;

	/**
	 * Persistence for blogs.
	 */
	private final BlogRepository blogRepository;

	/**
	 * Reused to store the assembled body once all chunks have arrived.
	 */
	private final FullRequestStrategy fullRequestStrategy;

	/**
	 * @return {@link ContentHandleType#CHUNK}
	 */
	@Override
	public ContentHandleType getContentHandleType() {
		return ContentHandleType.CHUNK;
	}

	/**
	 * Stores one chunk and, on the final chunk, assembles and persists the full body.
	 *
	 * <p>
	 * Transactional so storing the chunk and (on the last chunk) assembling, persisting
	 * and cleaning up happen atomically.
	 * </p>
	 * @param contentRequest the incoming chunk (must be a {@link ChunkContentRequest})
	 * @param blogPublicId the blog the chunk belongs to
	 * @throws InvalidRequestTypeException if the request is not a chunk request
	 */
	@Transactional
	@Override
	public void handle(BlogContentRequest contentRequest, UUID blogPublicId) {

		// Guard: this strategy only handles chunk requests.
		if (!(contentRequest instanceof ChunkContentRequest(long chunkSeq, String chunk, boolean isFinalChunk)))
			throw new InvalidRequestTypeException();

		ChunkContentRequest request = (ChunkContentRequest) contentRequest;

		// Map the request to a persistable chunk row and store it immediately.
		ChunkStore chunkStore = prepareChunkStore(request, blogPublicId);

		ChunkStore savedChunk = chunkStoreRepository.saveAndFlush(chunkStore);

		// Only once the final chunk lands, do we have the whole body to assemble.
		if (savedChunk.isFinalChunk()) {
			// Read every chunk back in sequence order and concatenate them.
			List<ChunkStore> chunkStores = chunkStoreRepository.findAllByBlogPublicId(blogPublicId);
			JsonNode assembledContent = assembleChunks(chunkStores);

			log.info("{} chunks for {} aggregated", chunkStores.size(), blogPublicId);
			// Reuse the full-content strategy to write the assembled body onto the blog.
			FullContentRequest fullContentRequest = FullContentRequest.builder().content(assembledContent).build();
			fullRequestStrategy.handle(fullContentRequest, blogPublicId);

			// The chunks are now redundant once the body is stored on the blog.
			cleanUpChunkStores(blogPublicId);

		}
	}

	/**
	 * Deletes all stored chunks for a blog after the body has been assembled.
	 * @param blogPublicId the blog whose chunks to remove
	 */
	private void cleanUpChunkStores(UUID blogPublicId) {
		chunkStoreRepository.deleteByBlogPublicId(blogPublicId);
	}

	/**
	 * Maps a chunk request into a persistable {@link ChunkStore} row.
	 * @param contentRequest the chunk request
	 * @param blogPublicId the owning blog's public id
	 * @return the chunk entity ready to persist
	 */
	private ChunkStore prepareChunkStore(ChunkContentRequest contentRequest, UUID blogPublicId) {
		return ChunkStore.builder()
			.blogPublicId(blogPublicId)
			.isFinalChunk(contentRequest.isFinalChunk())
			.chunkSeq(contentRequest.chunkSeq())
			.chunk(contentRequest.chunk())
			.build();
	}

	/**
	 * Concatenates ordered chunks into a single JSON content node.
	 * @param chunks the chunks, already ordered by sequence
	 * @return a JSON object with the joined content under a {@code "content"} field
	 */
	private JsonNode assembleChunks(List<ChunkStore> chunks) {

		String content = chunks.stream().map(ChunkStore::getChunk).collect(Collectors.joining());

		ObjectMapper mapper = new ObjectMapper();

		try {

			return mapper.readTree(content);

		}
		catch (Exception ex) {

			throw new RuntimeException("Failed to reconstruct blog content", ex);
		}
	}

}
