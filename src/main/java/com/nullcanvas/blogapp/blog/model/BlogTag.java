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
 * @author Anirudh
 * @since 01/06/26
 */
@Entity
@Table(name = "blog_tag", uniqueConstraints = {@UniqueConstraint(columnNames = {"blog_public_id", "tag_public_id"})})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogTag extends BaseEntity {

    @Column(name = "blog_public_id", nullable = false)
    private UUID blogPublicId;
    @Column(name = "tag_public_id", nullable = false)
    private UUID tagPublicId;

}
