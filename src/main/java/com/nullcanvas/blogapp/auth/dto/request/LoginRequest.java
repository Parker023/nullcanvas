package com.nullcanvas.blogapp.auth.dto.request;

import lombok.Data;

/**
 * @author Anirudh
 * @since 28/05/26
 */
@Data
public class LoginRequest {

	private String email;

	private String password;

}
