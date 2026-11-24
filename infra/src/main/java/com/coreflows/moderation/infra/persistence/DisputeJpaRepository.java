package com.coreflows.moderation.infra.persistence;

import com.coreflows.moderation.core.domain.model.DisputeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface DisputeJpaRepository extends JpaRepository<DisputeEntity, UUID> {

    List<DisputeEntity> findByStatusOrderByCreatedAtAsc(DisputeStatus status);

    List<DisputeEntity> findByRaisedByIdOrderByCreatedAtDesc(UUID raisedById);
}