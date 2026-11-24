package com.coreflows.moderation.core.application.port.out;

import com.coreflows.moderation.core.domain.model.Dispute;
import com.coreflows.moderation.core.domain.model.DisputeStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DisputeRepositoryPort {

    Dispute save(Dispute dispute);

    Optional<Dispute> findById(UUID id);

    List<Dispute> findByStatusOrderByCreatedAtAsc(DisputeStatus status, int limit, int offset);

    long countByStatus(DisputeStatus status);

    List<Dispute> findByRaisedByIdOrderByCreatedAtDesc(UUID raisedById, int limit, int offset);

    long countByRaisedById(UUID raisedById);
}