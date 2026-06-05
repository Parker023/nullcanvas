package com.nullcanvas.blogapp.blog.service.strategy;

import com.nullcanvas.blogapp.blog.dto.BlogStatus;
import com.nullcanvas.blogapp.blog.repository.BlogRepository;
import com.nullcanvas.blogapp.blog.dto.ContentHandleType;
import com.nullcanvas.blogapp.blog.dto.request.BlogContentRequest;
import com.nullcanvas.blogapp.blog.dto.request.FullContentRequest;
import com.nullcanvas.blogapp.blog.exception.BlogNotFoundException;
import com.nullcanvas.blogapp.blog.exception.InvalidRequestTypeException;
import com.nullcanvas.blogapp.blog.model.Blog;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 02/06/26
 */
@Service
@RequiredArgsConstructor
public class FullRequestStrategy implements BlogContentHandleStrategy {
    private final BlogRepository blogRepository;

    @Override
    public ContentHandleType getContentHandleType() {
        return ContentHandleType.FULL;
    }

    @Transactional
    @Override
    public void handle(BlogContentRequest contentRequest, UUID blogPublicId) {

        if (!(contentRequest instanceof FullContentRequest(String content)))
            throw new InvalidRequestTypeException();
        Blog blog = blogRepository.findByBlogPublicId(blogPublicId)
                .orElseThrow(BlogNotFoundException::new);

        Blog updated = blog.toBuilder()
                .contentJson(content)
                .contentSize((long) StringUtils.length(content))
                .status(BlogStatus.DRAFT)
                .build();

        blogRepository.save(updated);


    }
}
