package com.nullcanvas.blogapp.blog.service.reader.impl;

import com.nullcanvas.blogapp.blog.dto.response.FullContentResponse;
import com.nullcanvas.blogapp.blog.dto.response.MetadataResponse;
import com.nullcanvas.blogapp.blog.dto.response.TagDetailsResponse;
import com.nullcanvas.blogapp.blog.exception.BlogNotFoundException;
import com.nullcanvas.blogapp.blog.exception.ContentParsingException;
import com.nullcanvas.blogapp.blog.model.Blog;
import com.nullcanvas.blogapp.blog.repository.BlogRepository;
import com.nullcanvas.blogapp.blog.service.reader.FullContentReader;
import com.nullcanvas.blogapp.blog.service.tags.BlogTagManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the {@link FullContentReader} interface used for fetching and
 * constructing the full content of a blog entry, including metadata and content body,
 * based on a unique blog ID.
 * <p>
 * This service uses a {@link BlogRepository} to retrieve blog entries and an
 * {@link ObjectMapper} to parse the content body into JSON format. Throws
 * {@link BlogNotFoundException} if no blog is found with the specified ID.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FullContentReaderImpl implements FullContentReader {

    private final BlogRepository blogRepository;

    private final ObjectMapper objectMapper;

    private final BlogTagManager blogTagManager;

    /**
     * Reads the full content of a blog, including its metadata and content body, based on
     * the given blog ID. If no blog is found with the specified ID, a
     * {@code BlogNotFoundException} is thrown.
     *
     * @param blogId the unique identifier of the blog to retrieve
     * @return a {@link FullContentResponse} containing the metadata and content of the
     * blog
     * @throws BlogNotFoundException if the blog with the provided ID does not exist
     */
    @Override
    public FullContentResponse read(UUID blogId) {
        log.info("Reading full content for blog {}", blogId);
        Blog blog = blogRepository.findByPublicId(blogId).orElseThrow(BlogNotFoundException::new);
        return FullContentResponse.builder().metadata(toMetadata(blog)).content(parseContent(blog)).build();
    }

    /**
     * Converts a {@link Blog} object into a {@link MetadataResponse} object, extracting
     * and mapping its metadata attributes such as title, summary, slug, cover image URL,
     * visibility, status, content size, creation date, and public ID.
     *
     * @param blog the {@link Blog} object containing the metadata to be mapped
     * @return a {@link MetadataResponse} object containing the mapped metadata attributes
     */
    private MetadataResponse toMetadata(Blog blog) {

        List<TagDetailsResponse> tagsForBlog = blogTagManager.getTagsForBlog(blog.getPublicId());

        return MetadataResponse.builder()
                .title(blog.getTitle())
                .summary(blog.getSummary())
                .coverImageUrl(blog.getCoverImageUrl())
                .slug(blog.getSlug())
                .visibility(blog.getVisibility())
                .status(blog.getStatus())
                .contentSize(blog.getContentSize())
                .createdAt(blog.getCreatedAt())
                .publicId(blog.getPublicId())
                .tags(tagsForBlog)
                .build();
    }

    /**
     * Parses the content JSON string of a given {@link Blog} instance into a
     * {@link JsonNode}. If the parsing process encounters an error, a
     * {@code ContentParsingException} is thrown.
     *
     * @param blog the {@link Blog} instance containing the content JSON to be parsed
     * @return a {@link JsonNode} representation of the blog's content JSON
     * @throws ContentParsingException if a parsing error occurs
     */
    private JsonNode parseContent(Blog blog) {
        if (blog.getContentJson() == null) {
            return null;
        }
        try {
            return objectMapper.readTree(blog.getContentJson());
        } catch (JacksonException e) {
            log.error("Error while parsing content json for blog {}", blog.getPublicId(), e);
            throw new ContentParsingException(e.getMessage(), e);
        }
    }

}
