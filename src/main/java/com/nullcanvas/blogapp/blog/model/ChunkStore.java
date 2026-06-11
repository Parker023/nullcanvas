package com.nullcanvas.blogapp.blog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Persistent storage for an individual content chunk of a blog being uploaded
 * incrementally.
 *
 * <p>
 * Chunks for a blog are ordered by {@code chunkSeq}; the unique constraint on (blog, seq)
 * prevents duplicate sequence numbers. Once {@code isFinalChunk} arrives the chunks can
 * be assembled into the full body.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Entity
@Table(name = "chunk_store", uniqueConstraints = @UniqueConstraint(columnNames = { "blog_public_id", "chunk_seq" }))
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Getter
public class ChunkStore extends BaseEntity {

	/** Public id of the blog this chunk belongs to. */
	@Column(name = "blog_public_id", nullable = false)
	private UUID blogPublicId;

	/** Position of this chunk within the blog's content sequence. */
	@Column(name = "chunk_seq")
	private long chunkSeq;

	/** The raw content of this chunk; stored as a large object. */
	@Column(name = "chunk")
	@Lob
	private String chunk;

	/** Whether this is the final chunk, marking the upload complete. */
	@Column(name = "is_final_chunk")
	private boolean isFinalChunk;

}
