package com.nullcanvas.blogapp.auth.dto.request;

import com.nullcanvas.blogapp.auth.dto.AuthType;

/**
 * @author Anirudh
 * @since 25/05/26
 */
public interface AuthenticationRequest {

	AuthType getAuthType();

}
