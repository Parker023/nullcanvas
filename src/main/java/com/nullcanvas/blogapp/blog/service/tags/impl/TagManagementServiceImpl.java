package com.nullcanvas.blogapp.blog.service.tags.impl;

import com.nullcanvas.blogapp.blog.dto.request.CreateTagRequest;
import com.nullcanvas.blogapp.blog.dto.response.TagDetailsResponse;
import com.nullcanvas.blogapp.blog.exception.TagAlreadyExistsException;
import com.nullcanvas.blogapp.blog.model.Tag;
import com.nullcanvas.blogapp.blog.repository.TagRepository;
import com.nullcanvas.blogapp.blog.service.SlugGeneratorService;
import com.nullcanvas.blogapp.blog.service.tags.TagManagementService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Anirudh
 * @since 18/06/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TagManagementServiceImpl implements TagManagementService {

    private final TagRepository tagRepository;

    private final SlugGeneratorService slugGenerator;

    private ReadWriteLock lock;

    @PostConstruct
    public void init() {
        lock = new ReentrantReadWriteLock();
    }

    @Override
    @Transactional
    public TagDetailsResponse createTag(CreateTagRequest request) {

        String tagName = request.name();

        if (StringUtils.isBlank(tagName)) {
            throw new IllegalArgumentException("Tag name cannot be blank");
        }

        tagName = tagName.trim().toLowerCase();

        if (tagRepository.existsByName(tagName))
            throw new TagAlreadyExistsException(tagName);

        Tag tag = Tag.builder().name(tagName).slug(slugGenerator.generate(tagName)).build();

        tagRepository.save(tag);

        return TagDetailsResponse.builder().tagPublicId(tag.getPublicId()).name(tag.getName()).build();

    }

    @Override
    @Transactional
    public void deleteTag(UUID tagPublicId) {
        if (tagPublicId == null)
            throw new IllegalArgumentException("Tag public id cannot be null");

        tagRepository.deleteByPublicId(tagPublicId);

        log.info("Tag with public id {} deleted", tagPublicId);

    }

    @Override
    public List<TagDetailsResponse> getTags() {

        List<Tag> tags = tagRepository.findAll();
        log.info("Found {} tags", tags.size());
        if (tags.isEmpty())
            return List.of();

        return tags.stream()
                .map(tag -> TagDetailsResponse.builder().tagPublicId(tag.getPublicId()).name(tag.getName()).build())
                .toList();
    }

    @Override
    @Transactional
    public void handleUsage(List<UUID> tagPublicIds) {

        List<Tag> tagList = tagRepository.findAllByPublicIdIn(tagPublicIds);
        lock.writeLock().lock();
        try {
            tagList.forEach(tag -> tag.setUsageCount(tag.getUsageCount() + 1));
        } finally {
            lock.writeLock().unlock();
        }
    }

}
