package com.nullcanvas.blogapp.blog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Join entity linking a {@link Blog} to a {@link Tag} (many-to-many association row).
 *
 * <p>
 * The unique constraint on (blog, tag) prevents the same tag from being attached to a
 * blog twice.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Entity
@Table(name = "blog_tag",
		uniqueConstraints = { @UniqueConstraint(columnNames = { "blog_public_id", "tag_public_id" }) })
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogTag extends BaseEntity {

	/** Public id of the associated blog. */
	@Column(name = "blog_public_id", nullable = false)
	private UUID blogPublicId;

	/** Public id of the associated tag. */
	@Column(name = "tag_public_id", nullable = false)
	private UUID tagPublicId;

}
