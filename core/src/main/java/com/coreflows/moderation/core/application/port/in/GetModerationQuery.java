package com.coreflows.moderation.core.application.port.in;

import com.coreflows.moderation.core.domain.model.ActionType;
import com.coreflows.moderation.core.domain.model.ModerationAction;
import com.coreflows.moderation.core.domain.model.TargetType;

import java.util.UUID;

public interface GetModerationQuery {

    PageResult<ModerationAction> list(TargetType targetType, UUID targetId, ActionType action, PageQuery page);

    ModerationAction getById(UUID id);
}