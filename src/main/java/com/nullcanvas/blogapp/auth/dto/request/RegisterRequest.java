package com.nullcanvas.blogapp.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body for account registration; bean-validation constraints guard each field.
 *
 * @param name the user's display name; required, at most 100 characters
 * @param email the user's email; required, well-formed and at most 255 characters
 * @param password the chosen password; required, between 8 and 100 characters
 * @author Anirudh
 * @since 28/05/26
 */
public record RegisterRequest(

		@NotBlank @Size(max = 100) String name,

		@NotBlank @Email @Size(max = 255) String email,

		@NotBlank @Size(min = 8, max = 100) String password

) {
}
