package com.nullcanvas.blogapp.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * Request body describing a blog's metadata, submitted before any content is uploaded.
 *
 * @author Anirudh
 * @since 01/06/26
 */
@Builder
@Getter
@AllArgsConstructor
public class BlogMetadataRequest {

	/**
	 * The blog's title; required.
	 */
	@NotBlank(message = "title cannot be null")
	private String title;

	/**
	 * Optional short summary of the blog.
	 */
	private String summary;

	/**
	 * Optional URL of the blog's cover image.
	 */
	private String coverImageUrl;

	/**
	 * Visibility (public/private); required.
	 */
	@NotBlank(message = "visibility cannot be null")
	private String visibility;

	/**
	 * Optional list of tag names to associate with the blog.
	 */
	private List<UUID> tags;

	// TODO we are setting status as in_progress in beginning, what if user selected
	// published or draft in beginning?
	/**
	 * Optional initial lifecycle status; defaults to in-progress when omitted.
	 */
	private String status;

}
