package com.nullcanvas.blogapp.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @author Anirudh
 * @since 01/06/26
 */
@Builder
@Getter
@AllArgsConstructor
public class BlogMetadataRequest {

	@NotBlank(message = "title cannot be null")
	private String title;

	private String summary;

	private String coverImageUrl;

	@NotBlank(message = "visibility cannot be null")
	private String visibility;

	private List<String> tags;

	// TODO we are setting status as in_progress in beginning, what if user selected
	// published or draft in beginning?
	private String status;

}
