package com.coreflows.moderation.api.web.dto;

import com.coreflows.moderation.core.domain.model.ModerationAction;

import java.time.Instant;
import java.util.UUID;

public record ActionResponse(
        UUID id,
        String targetType,
        UUID targetId,
        String action,
        String reason,
        UUID moderatorId,
        Instant createdAt) {

    public static ActionResponse from(ModerationAction action) {
        return new ActionResponse(
                action.getId(),
                action.getTargetType().name(),
                action.getTargetId(),
                action.getAction().name(),
                action.getReason(),
                action.getModeratorId(),
                action.getCreatedAt());
    }
}