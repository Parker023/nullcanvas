package com.nullcanvas.blogapp.blog.dto;

/**
 * @author Anirudh
 * @since 17/06/26
 */
public enum RemoteUploadType {

	COVER_IMAGE, BLOG_CONTENT_IMAGE;

	public static RemoteUploadType fromString(String value) {
		return valueOf(value.toUpperCase());
	}

}