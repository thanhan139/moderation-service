package com.coreflows.moderation.core.application.port.in;

import com.coreflows.moderation.core.domain.model.ActionType;
import com.coreflows.moderation.core.domain.model.ModerationAction;
import com.coreflows.moderation.core.domain.model.TargetType;

import java.util.UUID;

public interface ModerateUserUseCase {

    ModerationAction raiseAction(Actor actor, RaiseActionCommand command);

    record RaiseActionCommand(TargetType targetType, UUID targetId, ActionType action, String reason) {
    }
}