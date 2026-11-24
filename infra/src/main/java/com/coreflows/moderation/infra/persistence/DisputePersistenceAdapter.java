package com.coreflows.moderation.infra.persistence;

import com.coreflows.moderation.core.application.port.out.DisputeRepositoryPort;
import com.coreflows.moderation.core.domain.model.Dispute;
import com.coreflows.moderation.core.domain.model.DisputeStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class DisputePersistenceAdapter implements DisputeRepositoryPort {

    private final DisputeJpaRepository jpaRepository;

    public DisputePersistenceAdapter(DisputeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Dispute save(Dispute dispute) {
        return jpaRepository.save(DisputeEntity.from(dispute)).toDomain();
    }

    @Override
    public Optional<Dispute> findById(UUID id) {
        return jpaRepository.findById(id).map(DisputeEntity::toDomain);
    }

    @Override
    public List<Dispute> findByStatusOrderByCreatedAtAsc(DisputeStatus status, int limit, int offset) {
        return jpaRepository.findByStatusOrderByCreatedAtAsc(status).stream()
                .skip(offset).limit(limit).map(DisputeEntity::toDomain).toList();
    }

    @Override
    public long countByStatus(DisputeStatus status) {
        return jpaRepository.findByStatusOrderByCreatedAtAsc(status).size();
    }

    @Override
    public List<Dispute> findByRaisedByIdOrderByCreatedAtDesc(UUID raisedById, int limit, int offset) {
        return jpaRepository.findByRaisedByIdOrderByCreatedAtDesc(raisedById).stream()
                .skip(offset).limit(limit).map(DisputeEntity::toDomain).toList();
    }

    @Override
    public long countByRaisedById(UUID raisedById) {
        return jpaRepository.findByRaisedByIdOrderByCreatedAtDesc(raisedById).size();
    }
}