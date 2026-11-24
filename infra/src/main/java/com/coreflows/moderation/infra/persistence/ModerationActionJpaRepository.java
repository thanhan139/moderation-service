package com.coreflows.moderation.infra.persistence;

import com.coreflows.moderation.core.domain.model.ActionType;
import com.coreflows.moderation.core.domain.model.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface ModerationActionJpaRepository extends JpaRepository<ModerationActionEntity, UUID> {

    List<ModerationActionEntity> findByTargetTypeAndTargetIdOrderByCreatedAtDesc(
            TargetType targetType, UUID targetId);

    Optional<ModerationActionEntity> findFirstByTargetTypeAndTargetIdOrderByCreatedAtDesc(
            TargetType targetType, UUID targetId);
}