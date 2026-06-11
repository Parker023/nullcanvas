package com.nullcanvas.blogapp.auth.controller;

import com.nullcanvas.blogapp.auth.dto.request.JwtAuthenticationRequest;
import com.nullcanvas.blogapp.auth.dto.request.LoginRequest;
import com.nullcanvas.blogapp.auth.dto.request.RegisterRequest;
import com.nullcanvas.blogapp.auth.dto.response.AuthenticationResponse;
import com.nullcanvas.blogapp.auth.dto.response.CurrentUserResponse;
import com.nullcanvas.blogapp.auth.security.principal.CustomUserPrincipal;
import com.nullcanvas.blogapp.auth.service.AuthenticationService;
import com.nullcanvas.blogapp.auth.service.UserRegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Objects;

import static com.nullcanvas.blogapp.auth.constants.AuthConstants.ACCESS_TOKEN;

/**
 * REST endpoints for credential-based authentication under {@code /auth/v1}.
 *
 * <p>
 * Handles login, registration, logout and retrieval of the currently authenticated user.
 * The access token is never returned in the body; it is delivered as an {@code HttpOnly},
 * {@code Secure} cookie so that browser JavaScript cannot read it, mitigating token theft
 * via XSS.
 * </p>
 *
 * @author Anirudh
 * @since 17/05/26
 */

@RestController
@RequestMapping("/auth/v1")
@RequiredArgsConstructor
public class AuthController {

	/** Verifies credentials and issues an authentication token. */
	private final AuthenticationService authenticationService;

	/** Creates new user accounts and drives the email-verification flow. */
	private final UserRegistrationService registrationService;

	/**
	 * Lightweight health-check endpoint used to confirm the service is reachable.
	 * @return a static "up and running" message
	 */
	@GetMapping("/test")
	public String test() {
		return "null canvas App Up and Running";
	}

	/**
	 * Authenticates a user with email/password and sets the access token as a secure
	 * cookie.
	 * @param request the login credentials
	 * @param response the servlet response the token cookie is written to
	 * @return {@code 200 OK} with the {@code Set-Cookie} header on success
	 */
	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {

		// Wrap the raw credentials in the JWT-specific request the authentication service
		// expects.
		JwtAuthenticationRequest authRequest = new JwtAuthenticationRequest(request.getEmail(), request.getPassword());

		// Validate the credentials and obtain the signed access token.
		AuthenticationResponse authResponse = authenticationService.authenticate(authRequest);

		// Build the token cookie: HttpOnly blocks JS access, Secure restricts it to
		// HTTPS,
		// SameSite=Lax limits cross-site sending, and it expires after one day.
		ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN, authResponse.getToken())
			.httpOnly(true)
			.secure(true)
			.path("/")
			.sameSite("Lax")
			.maxAge(Duration.ofDays(1))
			.build();

		// Attach the cookie so the browser stores and replays it on subsequent requests.
		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return ResponseEntity.ok().build();
	}

	/**
	 * Registers a new user account; the request body is validated before processing.
	 * @param request the registration details (name, email, password)
	 * @return {@code 201 Created} once the account is created and verification is
	 * initiated
	 */
	@PostMapping("/register")
	public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {

		// Create the account and trigger the email-verification (OTP) flow.
		registrationService.register(request);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * Logs the user out by overwriting the access-token cookie with an
	 * immediately-expiring one.
	 * @param response the servlet response the cleared cookie is written to
	 * @return {@code 200 OK} with the cookie-clearing {@code Set-Cookie} header
	 */
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {

		// Emit an empty token cookie with maxAge=0 so the browser deletes the stored
		// token.
		ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN, null)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		return ResponseEntity.ok().build();
	}

	/**
	 * Returns the profile of the currently authenticated user.
	 * @param authentication the security context's authentication, {@code null} when
	 * unauthenticated
	 * @return the current user's name, email and roles, or {@code 401} when no user is
	 * authenticated
	 */
	@GetMapping("/me")
	public ResponseEntity<CurrentUserResponse> me(Authentication authentication) {

		// No authentication in the security context means the caller is not logged in.
		if (authentication == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// The filter chain stores our custom principal; cast it to read the user's
		// details.
		CustomUserPrincipal user = (CustomUserPrincipal) authentication.getPrincipal();

		// Map the principal into the response DTO, flattening granted authorities into
		// role strings.
		return ResponseEntity.ok(CurrentUserResponse.builder()
			.name(Objects.requireNonNull(user).getUsername())
			.email(user.getEmail())
			.roles(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
			.build());
	}

}
