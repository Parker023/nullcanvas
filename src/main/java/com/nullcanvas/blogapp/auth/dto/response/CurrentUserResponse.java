package com.nullcanvas.blogapp.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author Anirudh
 * @since 03/06/26
 */

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class CurrentUserResponse {

	private String name;

	private String email;

	private List<String> roles;

}
