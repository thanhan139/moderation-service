package com.coreflows.moderation.infra.persistence;

import com.coreflows.moderation.core.domain.model.Dispute;
import com.coreflows.moderation.core.domain.model.DisputeCategory;
import com.coreflows.moderation.core.domain.model.DisputeDecision;
import com.coreflows.moderation.core.domain.model.DisputeStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "disputes")
public class DisputeEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "campaign_id", nullable = false)
    private UUID campaignId;

    @Column(name = "task_id")
    private UUID taskId;

    @Column(name = "raised_by_id", nullable = false)
    private UUID raisedById;

    @Column(name = "moderator_id")
    private UUID moderatorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private DisputeCategory category;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DisputeStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision")
    private DisputeDecision decision;

    @Column(name = "resolution")
    private String resolution;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    protected DisputeEntity() {
    }

    public static DisputeEntity from(Dispute d) {
        DisputeEntity e = new DisputeEntity();
        e.id = d.getId();
        e.campaignId = d.getCampaignId();
        e.taskId = d.getTaskId();
        e.raisedById = d.getRaisedById();
        e.moderatorId = d.getModeratorId();
        e.category = d.getCategory();
        e.description = d.getDescription();
        e.status = d.getStatus();
        e.decision = d.getDecision();
        e.resolution = d.getResolution();
        e.createdAt = LocalDateTime.ofInstant(d.getCreatedAt(), ZoneOffset.UTC);
        e.resolvedAt = d.getResolvedAt() == null ? null : LocalDateTime.ofInstant(d.getResolvedAt(), ZoneOffset.UTC);
        return e;
    }

    public Dispute toDomain() {
        Instant resolved = resolvedAt == null ? null : resolvedAt.toInstant(ZoneOffset.UTC);
        return Dispute.restore(id, campaignId, taskId, raisedById, moderatorId,
                category, description, status, decision, resolution,
                createdAt.toInstant(ZoneOffset.UTC), resolved);
    }
}