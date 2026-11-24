package com.coreflows.moderation.core.domain.model;

import com.coreflows.moderation.core.domain.exception.BadRequestException;
import com.coreflows.moderation.core.domain.exception.OperationNotPermittedException;

import java.time.Instant;
import java.util.UUID;

public final class Dispute {

    private final UUID id;
    private final UUID campaignId;
    private final UUID taskId;
    private final UUID raisedById;
    private final UUID moderatorId;
    private final DisputeCategory category;
    private final String description;
    private final DisputeStatus status;
    private final DisputeDecision decision;
    private final String resolution;
    private final Instant createdAt;
    private final Instant resolvedAt;

    private Dispute(UUID id, UUID campaignId, UUID taskId, UUID raisedById, UUID moderatorId,
                    DisputeCategory category, String description, DisputeStatus status,
                    DisputeDecision decision, String resolution, Instant createdAt, Instant resolvedAt) {
        this.id = id;
        this.campaignId = campaignId;
        this.taskId = taskId;
        this.raisedById = raisedById;
        this.moderatorId = moderatorId;
        this.category = category;
        this.description = description;
        this.status = status;
        this.decision = decision;
        this.resolution = resolution;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
    }

    public static Dispute raise(UUID campaignId, UUID taskId, UUID raisedById,
                                 DisputeCategory category, String description, Instant now) {
        if (campaignId == null) {
            throw new BadRequestException("campaign_id là bắt buộc");
        }
        if (raisedById == null) {
            throw new BadRequestException("raised_by_id là bắt buộc");
        }
        if (category == null) {
            throw new BadRequestException("category là bắt buộc");
        }
        String clean = description == null ? null : description.trim();
        if (clean == null || clean.isBlank()) {
            throw new BadRequestException("description không được để trống");
        }
        Instant nowRef = now == null ? Instant.now() : now;
        return new Dispute(UUID.randomUUID(), campaignId, taskId, raisedById, null,
                category, clean, DisputeStatus.OPEN, null, null, nowRef, null);
    }

    public Dispute takeCase(UUID moderatorId) {
        if (moderatorId == null) {
            throw new BadRequestException("moderator_id là bắt buộc");
        }
        if (status != DisputeStatus.OPEN) {
            throw new OperationNotPermittedException("Chỉ có thể nhận khiếu nại ở trạng thái open");
        }
        return new Dispute(id, campaignId, taskId, raisedById, moderatorId,
                category, description, DisputeStatus.IN_REVIEW, null, null, createdAt, null);
    }

    public Dispute close(DisputeDecision decision, String resolution, Instant now) {
        if (status != DisputeStatus.IN_REVIEW) {
            throw new OperationNotPermittedException("Chỉ có thể đóng khiếu nại ở trạng thái in_review");
        }
        if (decision == null) {
            throw new BadRequestException("decision là bắt buộc khi đóng khiếu nại");
        }
        String cleanResolution = resolution == null ? null : resolution.trim();
        if (cleanResolution == null || cleanResolution.isBlank()) {
            throw new BadRequestException("resolution không được để trống khi đóng khiếu nại");
        }
        Instant nowRef = now == null ? Instant.now() : now;
        return new Dispute(id, campaignId, taskId, raisedById, moderatorId,
                category, description, DisputeStatus.CLOSED, decision, cleanResolution, createdAt, nowRef);
    }

    public static Dispute restore(UUID id, UUID campaignId, UUID taskId, UUID raisedById, UUID moderatorId,
                                   DisputeCategory category, String description, DisputeStatus status,
                                   DisputeDecision decision, String resolution, Instant createdAt, Instant resolvedAt) {
        return new Dispute(id, campaignId, taskId, raisedById, moderatorId,
                category, description, status, decision, resolution, createdAt, resolvedAt);
    }

    public UUID getId() { return id; }
    public UUID getCampaignId() { return campaignId; }
    public UUID getTaskId() { return taskId; }
    public UUID getRaisedById() { return raisedById; }
    public UUID getModeratorId() { return moderatorId; }
    public DisputeCategory getCategory() { return category; }
    public String getDescription() { return description; }
    public DisputeStatus getStatus() { return status; }
    public DisputeDecision getDecision() { return decision; }
    public String getResolution() { return resolution; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getResolvedAt() { return resolvedAt; }
}