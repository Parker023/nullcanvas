package com.nullcanvas.blogapp.blog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistent tag, mapped to the {@code tag_store} table.
 *
 * <p>
 * Tag names are unique so the same label is shared across blogs rather than duplicated,
 * with blogs linked to tags through {@link BlogTag}.
 * </p>
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Entity
@Table(name = "tag_store")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag extends BaseEntity {

	/** The tag's unique label. */
	@Column(name = "tag_name", nullable = false, unique = true)
	private String name;

}
