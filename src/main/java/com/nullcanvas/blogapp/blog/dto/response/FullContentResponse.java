package com.nullcanvas.blogapp.blog.dto.response;

import lombok.Builder;
import tools.jackson.databind.JsonNode;

@Builder
public record FullContentResponse(MetadataResponse metadata, JsonNode content) implements BlogContentResponse {
}