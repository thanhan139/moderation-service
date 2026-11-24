package com.coreflows.moderation.infra.persistence;

import com.coreflows.moderation.core.domain.model.ActionType;
import com.coreflows.moderation.core.domain.model.ModerationAction;
import com.coreflows.moderation.core.domain.model.TargetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "moderation_actions")
public class ModerationActionEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;

    @Column(name = "target_id", nullable = false)
    private UUID targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ActionType action;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "moderator_id", nullable = false)
    private UUID moderatorId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected ModerationActionEntity() {
    }

    public static ModerationActionEntity from(ModerationAction action) {
        ModerationActionEntity entity = new ModerationActionEntity();
        entity.id = action.getId();
        entity.targetType = action.getTargetType();
        entity.targetId = action.getTargetId();
        entity.action = action.getAction();
        entity.reason = action.getReason();
        entity.moderatorId = action.getModeratorId();
        entity.createdAt = LocalDateTime.ofInstant(action.getCreatedAt(), ZoneOffset.UTC);
        return entity;
    }

    public ModerationAction toDomain() {
        return ModerationAction.restore(id, targetType, targetId, action, reason, moderatorId,
                createdAt.toInstant(ZoneOffset.UTC));
    }

    public ActionType getAction() { return action; }
}