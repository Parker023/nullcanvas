package com.nullcanvas.blogapp.auth.security.filter;

import com.nullcanvas.blogapp.auth.jwt.JwtService;
import com.nullcanvas.blogapp.auth.security.principal.CustomUserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.nullcanvas.blogapp.auth.constants.AuthConstants.ACCESS_TOKEN;
import static com.nullcanvas.blogapp.auth.constants.AuthConstants.EMAIL;
import static com.nullcanvas.blogapp.auth.constants.AuthConstants.ROLE;


/**
 * @author Anirudh
 * @since 28/05/26
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessTokenFromCookie = getAccessTokenFromCookie(request.getCookies());


        log.info("access token: {}", accessTokenFromCookie);

        if (StringUtils.isEmpty(accessTokenFromCookie)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {


            if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                Claims claims = jwtService.extractClaims(accessTokenFromCookie);
                UUID userId = UUID.fromString(claims.getSubject());
                String email =
                        claims.get(EMAIL, String.class);
                String role = claims.get(ROLE, String.class);
                UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(userId, email, role);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception ex) {

            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);


    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        return HttpMethod.OPTIONS.matches(request.getMethod());
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(UUID userId, String email, String role) {
        CustomUserPrincipal principal =
                new CustomUserPrincipal(
                        userId,
                        email,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(role)
                        )
                );

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
    }

    private String getAccessTokenFromCookie(
            Cookie[] cookies
    ) {

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {

            if (ACCESS_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
