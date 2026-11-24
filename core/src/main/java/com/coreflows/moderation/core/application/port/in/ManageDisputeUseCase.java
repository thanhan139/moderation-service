package com.coreflows.moderation.core.application.port.in;

import com.coreflows.moderation.core.domain.model.Dispute;
import com.coreflows.moderation.core.domain.model.DisputeCategory;
import com.coreflows.moderation.core.domain.model.DisputeDecision;

import java.util.UUID;

public interface ManageDisputeUseCase {

    Dispute raiseDispute(Actor actor, RaiseDisputeCommand command);

    Dispute takeCase(UUID disputeId, Actor actor);

    Dispute closeDispute(UUID disputeId, Actor actor, CloseDisputeCommand command);

    record RaiseDisputeCommand(UUID campaignId, UUID taskId, DisputeCategory category, String description) {
    }

    record CloseDisputeCommand(DisputeDecision decision, String resolution) {
    }
}