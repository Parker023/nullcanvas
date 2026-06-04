package com.nullcanvas.blogapp.blog.util;

import com.nullcanvas.blogapp.auth.security.principal.CustomUserPrincipal;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Anirudh
 * @since 01/06/26
 */
@Component
public class AuthUtil {
    public UUID getAuthorIdFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            throw new BadCredentialsException("User not authenticated");

        if (authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            return principal.getUserPublicId();
        }

        throw new BadCredentialsException("User not authenticated");
    }
}
