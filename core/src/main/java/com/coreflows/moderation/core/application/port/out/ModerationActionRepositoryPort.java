package com.coreflows.moderation.core.application.port.out;

import com.coreflows.moderation.core.domain.model.ActionType;
import com.coreflows.moderation.core.domain.model.ModerationAction;
import com.coreflows.moderation.core.domain.model.TargetType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModerationActionRepositoryPort {

    ModerationAction save(ModerationAction action);

    Optional<ModerationAction> findById(UUID id);

    List<ModerationAction> findByTarget(TargetType targetType, UUID targetId, ActionType action, int limit, int offset);

    long countByTarget(TargetType targetType, UUID targetId, ActionType action);

    Optional<ModerationAction> findLatestByTarget(TargetType targetType, UUID targetId);
}