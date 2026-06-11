package com.nullcanvas.blogapp.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for auth-module JPA entities, providing identity and audit columns.
 *
 * <p>
 * Each entity carries an internal numeric primary key plus an externally-safe
 * {@code publicId}, and the {@link AuditingEntityListener} automatically maintains the
 * created/updated timestamps.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class BaseEntity {

	/** Auto-incremented internal primary key; never exposed to clients. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Stable, unique identifier exposed externally so the internal id stays private. */
	@Column(nullable = false, unique = true, updatable = false)
	private UUID publicId;

	/** Timestamp set once when the row is first persisted. */
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	/** Timestamp refreshed on every update. */
	@LastModifiedDate
	@Column(nullable = false)
	private Instant updatedAt;

	/**
	 * Assigns a random {@code publicId} before the entity is first inserted, if one isn't
	 * set.
	 */
	@PrePersist
	public void prePersist() {

		// Generate the public id lazily at insert time so callers never have to supply
		// it.
		if (publicId == null) {
			publicId = UUID.randomUUID();
		}
	}

}
