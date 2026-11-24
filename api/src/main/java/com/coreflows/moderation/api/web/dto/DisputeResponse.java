package com.coreflows.moderation.api.web.dto;

import com.coreflows.moderation.core.domain.model.Dispute;

import java.time.Instant;
import java.util.UUID;

public record DisputeResponse(
        UUID id,
        UUID campaignId,
        UUID taskId,
        UUID raisedById,
        UUID moderatorId,
        String category,
        String description,
        String status,
        String decision,
        String resolution,
        Instant createdAt,
        Instant resolvedAt) {

    public static DisputeResponse from(Dispute d) {
        return new DisputeResponse(
                d.getId(), d.getCampaignId(), d.getTaskId(),
                d.getRaisedById(), d.getModeratorId(),
                d.getCategory().name(), d.getDescription(),
                d.getStatus().name(),
                d.getDecision() == null ? null : d.getDecision().name(),
                d.getResolution(),
                d.getCreatedAt(), d.getResolvedAt());
    }
}