package com.coreflows.moderation.core.domain.model;

import com.coreflows.moderation.core.domain.exception.BadRequestException;

import java.time.Instant;
import java.util.UUID;

public final class ModerationAction {

    private final UUID id;
    private final TargetType targetType;
    private final UUID targetId;
    private final ActionType action;
    private final String reason;
    private final UUID moderatorId;
    private final Instant createdAt;

    private ModerationAction(UUID id, TargetType targetType, UUID targetId, ActionType action,
                             String reason, UUID moderatorId, Instant createdAt) {
        this.id = id;
        this.targetType = targetType;
        this.targetId = targetId;
        this.action = action;
        this.reason = reason;
        this.moderatorId = moderatorId;
        this.createdAt = createdAt;
    }

    public static ModerationAction raise(TargetType targetType, UUID targetId, ActionType action,
                                          String reason, UUID moderatorId, Instant now) {
        if (targetType == null || targetId == null || action == null || moderatorId == null) {
            throw new BadRequestException("targetType, targetId, action, moderatorId không được để trống");
        }
        String cleanReason = reason == null ? null : reason.trim();
        if (cleanReason == null || cleanReason.isBlank()) {
            throw new BadRequestException("reason bắt buộc khi tạo moderation action");
        }
        Instant nowRef = now == null ? Instant.now() : now;
        return new ModerationAction(UUID.randomUUID(), targetType, targetId, action, cleanReason, moderatorId, nowRef);
    }

    public static ModerationAction restore(UUID id, TargetType targetType, UUID targetId, ActionType action,
                                            String reason, UUID moderatorId, Instant createdAt) {
        return new ModerationAction(id, targetType, targetId, action, reason, moderatorId, createdAt);
    }

    public UUID getId() { return id; }
    public TargetType getTargetType() { return targetType; }
    public UUID getTargetId() { return targetId; }
    public ActionType getAction() { return action; }
    public String getReason() { return reason; }
    public UUID getModeratorId() { return moderatorId; }
    public Instant getCreatedAt() { return createdAt; }
}