package com.coreflows.moderation.infra.persistence;

import com.coreflows.moderation.core.application.port.out.ModerationActionRepositoryPort;
import com.coreflows.moderation.core.domain.model.ActionType;
import com.coreflows.moderation.core.domain.model.ModerationAction;
import com.coreflows.moderation.core.domain.model.TargetType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class ModerationActionPersistenceAdapter implements ModerationActionRepositoryPort {

    private final ModerationActionJpaRepository jpaRepository;

    public ModerationActionPersistenceAdapter(ModerationActionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public ModerationAction save(ModerationAction action) {
        return jpaRepository.save(ModerationActionEntity.from(action)).toDomain();
    }

    @Override
    public Optional<ModerationAction> findById(UUID id) {
        return jpaRepository.findById(id).map(ModerationActionEntity::toDomain);
    }

    @Override
    public List<ModerationAction> findByTarget(TargetType targetType, UUID targetId,
                                                ActionType action, int limit, int offset) {
        var all = jpaRepository.findByTargetTypeAndTargetIdOrderByCreatedAtDesc(targetType, targetId);
        return all.stream()
                .filter(e -> action == null || e.getAction() == action)
                .skip(offset)
                .limit(limit)
                .map(ModerationActionEntity::toDomain)
                .toList();
    }

    @Override
    public long countByTarget(TargetType targetType, UUID targetId, ActionType action) {
        if (action == null) {
            return jpaRepository.findByTargetTypeAndTargetIdOrderByCreatedAtDesc(targetType, targetId).size();
        }
        return jpaRepository.findByTargetTypeAndTargetIdOrderByCreatedAtDesc(targetType, targetId).stream()
                .filter(e -> e.getAction() == action)
                .count();
    }

    @Override
    public Optional<ModerationAction> findLatestByTarget(TargetType targetType, UUID targetId) {
        return jpaRepository
                .findFirstByTargetTypeAndTargetIdOrderByCreatedAtDesc(targetType, targetId)
                .map(ModerationActionEntity::toDomain);
    }
}