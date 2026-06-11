package com.nullcanvas.blogapp.blog.service.reader;

import com.nullcanvas.blogapp.blog.dto.response.FullContentResponse;

import java.util.UUID;

public interface FullContentReader {

	FullContentResponse read(UUID blogId);

}