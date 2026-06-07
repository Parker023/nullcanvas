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

	@Column(name = "blog_public_id", nullable = false)
	private UUID blogPublicId;

	@Column(name = "chunk_seq")
	private long chunkSeq;

	@Column(name = "chunk")
	@Lob
	private String chunk;

	@Column(name = "is_final_chunk")
	private boolean isFinalChunk;

}
