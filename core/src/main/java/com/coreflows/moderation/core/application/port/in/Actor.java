package com.coreflows.moderation.core.application.port.in;

import java.util.UUID;

public record Actor(UUID userId, String role) {
    public boolean isAdmin() {
        return "admin".equals(role);
    }
    public boolean isModerator() {
        return "moderator".equals(role);
    }
    public boolean canModerate() {
        return isAdmin() || isModerator();
    }
}