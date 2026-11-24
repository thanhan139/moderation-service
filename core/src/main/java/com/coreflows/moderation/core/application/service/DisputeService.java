package com.coreflows.moderation.core.application.service;

import com.coreflows.moderation.core.application.port.in.Actor;
import com.coreflows.moderation.core.application.port.in.GetDisputeQuery;
import com.coreflows.moderation.core.application.port.in.ManageDisputeUseCase;
import com.coreflows.moderation.core.application.port.in.PageQuery;
import com.coreflows.moderation.core.application.port.in.PageResult;
import com.coreflows.moderation.core.application.port.out.DisputeRepositoryPort;
import com.coreflows.moderation.core.domain.exception.DisputeNotFoundException;
import com.coreflows.moderation.core.domain.exception.OperationNotPermittedException;
import com.coreflows.moderation.core.domain.model.Dispute;
import com.coreflows.moderation.core.domain.model.DisputeStatus;

import java.time.Clock;
import java.util.UUID;

public class DisputeService implements ManageDisputeUseCase, GetDisputeQuery {

    private final DisputeRepositoryPort repository;
    private final Clock clock;

    public DisputeService(DisputeRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public Dispute raiseDispute(Actor actor, RaiseDisputeCommand command) {
        Dispute dispute = Dispute.raise(
                command.campaignId(), command.taskId(), actor.userId(),
                command.category(), command.description(), clock.instant());
        return repository.save(dispute);
    }

    @Override
    public Dispute takeCase(UUID disputeId, Actor actor) {
        assertCanModerate(actor);
        Dispute dispute = repository.findById(disputeId)
                .orElseThrow(() -> new DisputeNotFoundException("Không tìm thấy khiếu nại " + disputeId));
        Dispute taken = dispute.takeCase(actor.userId());
        return repository.save(taken);
    }

    @Override
    public Dispute closeDispute(UUID disputeId, Actor actor, CloseDisputeCommand command) {
        assertCanModerate(actor);
        Dispute dispute = repository.findById(disputeId)
                .orElseThrow(() -> new DisputeNotFoundException("Không tìm thấy khiếu nại " + disputeId));
        Dispute closed = dispute.close(command.decision(), command.resolution(), clock.instant());
        return repository.save(closed);
    }

    @Override
    public PageResult<Dispute> listQueue(DisputeStatus status, PageQuery page) {
        var items = repository.findByStatusOrderByCreatedAtAsc(status, page.size(), page.page() * page.size());
        var total = repository.countByStatus(status);
        return new PageResult<>(items, page.page(), page.size(), total);
    }

    @Override
    public PageResult<Dispute> listMine(UUID userId, PageQuery page) {
        var items = repository.findByRaisedByIdOrderByCreatedAtDesc(userId, page.size(), page.page() * page.size());
        var total = repository.countByRaisedById(userId);
        return new PageResult<>(items, page.page(), page.size(), total);
    }

    @Override
    public Dispute getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new DisputeNotFoundException("Không tìm thấy khiếu nại " + id));
    }

    private void assertCanModerate(Actor actor) {
        if (actor == null || !actor.canModerate()) {
            throw new OperationNotPermittedException("Chỉ ADMIN hoặc MODERATOR mới được thực hiện thao tác này");
        }
    }
}