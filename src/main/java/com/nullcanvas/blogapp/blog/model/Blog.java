package com.nullcanvas.blogapp.blog.model;

import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.dto.Visibility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * @author Anirudh
 * @since 01/06/26
 */

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog extends BaseEntity {

    @Column(name = "author_id", nullable = false)
    private UUID authorPublicId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "summary")
    private String summary;

    @Column(name = "content")
    @Lob
    private String contentJson;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private BlogStatus status;

    @Column(name = "visibility",nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "content_size")
    private Long contentSize;

}
