package com.nullcanvas.blogapp.blog.exception;

/**
 * @author Anirudh
 * @since 18/06/26
 */
public class TagAlreadyExistsException extends RuntimeException {

	public TagAlreadyExistsException(String tagName) {
		super("Tag with name " + tagName + " already exists");
	}

}
