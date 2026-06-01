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
public class TagStore extends BaseEntity {
    @Column(name = "tag_name", nullable = false, unique = true)
    private String name;
}
