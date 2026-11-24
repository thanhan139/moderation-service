package com.coreflows.moderation.api.security;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Locale;
import java.util.UUID;

public final class TokenRoles {

    private TokenRoles() {}

    public static UUID userId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    public static boolean isAdmin(Jwt jwt) {
        String role = jwt.getClaimAsString(TokenClaims.ROLE);
        return role != null && "admin".equals(role.toLowerCase(Locale.ROOT));
    }

    public static boolean isModerator(Jwt jwt) {
        String role = jwt.getClaimAsString(TokenClaims.ROLE);
        return role != null && "moderator".equals(role.toLowerCase(Locale.ROOT));
    }

    public static boolean canModerate(Jwt jwt) {
        return isAdmin(jwt) || isModerator(jwt);
    }
}