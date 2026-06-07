package com.nullcanvas.blogapp.blog.model;

import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.dto.Visibility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

/**
 * Persistent blog post, mapped to the {@code blogs} table.
 *
 * <p>
 * Stores both metadata (title, slug, visibility, status) and the assembled body as a
 * JSONB column. Inherits identity and audit columns from {@link BaseEntity}.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Blog extends BaseEntity {

	/** Public id of the user who authored the blog. */
	@Column(name = "author_id", nullable = false)
	private UUID authorPublicId;

	/** The blog's title. */
	@Column(name = "title", nullable = false)
	private String title;

	/** URL-friendly slug derived from the title. */
	@Column(name = "slug", nullable = false)
	private String slug;

	/** Optional short summary. */
	@Column(name = "summary")
	private String summary;

	/** The assembled blog body, stored as JSONB in Postgres. */
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "content", columnDefinition = "jsonb")
	private String contentJson;

	/** Optional cover image URL. */
	@Column(name = "cover_image_url")
	private String coverImageUrl;

	/** Lifecycle status, persisted as its enum name. */
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private BlogStatus status;

	/** Visibility (public/private), persisted as its enum name. */
	@Column(name = "visibility", nullable = false)
	@Enumerated(EnumType.STRING)
	private Visibility visibility;

	/** When the blog was published, if it has been. */
	@Column(name = "published_at")
	private Instant publishedAt;

	/** Size of the stored content, in bytes/characters. */
	@Column(name = "content_size")
	private Long contentSize;

}
