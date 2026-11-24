package com.coreflows.moderation.api.web.dto;

import com.coreflows.moderation.core.domain.model.ActionType;
import com.coreflows.moderation.core.domain.model.TargetType;

import java.util.UUID;

public record RaiseActionRequest(
        String targetType,
        UUID targetId,
        String action,
        String reason) {

    public TargetType targetTypeEnum() {
        return TargetType.valueOf(targetType);
    }

    public ActionType actionEnum() {
        return ActionType.valueOf(action);
    }
}