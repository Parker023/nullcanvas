package com.nullcanvas.blogapp.blog.dto.request;

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

    private String title;
    private String summary;
    private String coverImageUrl;
    private String visibility;
    private List<String> tags;

}
