package com.coreflows.moderation.core.application.service;

import com.coreflows.moderation.core.application.port.in.Actor;
import com.coreflows.moderation.core.application.port.in.GetModerationQuery;
import com.coreflows.moderation.core.application.port.in.ModerateUserUseCase;
import com.coreflows.moderation.core.application.port.in.PageQuery;
import com.coreflows.moderation.core.application.port.in.PageResult;
import com.coreflows.moderation.core.application.port.out.ModerationActionRepositoryPort;
import com.coreflows.moderation.core.domain.exception.ModerationActionNotFoundException;
import com.coreflows.moderation.core.domain.exception.OperationNotPermittedException;
import com.coreflows.moderation.core.domain.model.ActionType;
import com.coreflows.moderation.core.domain.model.ModerationAction;
import com.coreflows.moderation.core.domain.model.TargetType;

import java.time.Clock;
import java.util.UUID;

public class ModerationService implements ModerateUserUseCase, GetModerationQuery {

    private final ModerationActionRepositoryPort repository;
    private final Clock clock;

    public ModerationService(ModerationActionRepositoryPort repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public ModerationAction raiseAction(Actor actor, RaiseActionCommand command) {
        assertCanModerate(actor);
        ModerationAction action = ModerationAction.raise(
                command.targetType(), command.targetId(), command.action(),
                command.reason(), actor.userId(), clock.instant());
        return repository.save(action);
    }

    @Override
    public PageResult<ModerationAction> list(TargetType targetType, UUID targetId, ActionType action, PageQuery page) {
        var items = repository.findByTarget(targetType, targetId, action, page.size(), page.page() * page.size());
        var total = repository.countByTarget(targetType, targetId, action);
        return new PageResult<>(items, page.page(), page.size(), total);
    }

    @Override
    public ModerationAction getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ModerationActionNotFoundException("Không tìm thấy action " + id));
    }

    private void assertCanModerate(Actor actor) {
        if (actor == null || !actor.canModerate()) {
            throw new OperationNotPermittedException("Chỉ ADMIN hoặc MODERATOR mới được thực hiện thao tác này");
        }
    }
}