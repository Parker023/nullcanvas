package com.nullcanvas.blogapp.blog.dto.response;

import tools.jackson.databind.JsonNode;

public record ChunkContentResponse(MetadataResponse metadata, JsonNode chunkContent, Integer chunkNumber,
		Integer totalChunks) implements BlogContentResponse {
}