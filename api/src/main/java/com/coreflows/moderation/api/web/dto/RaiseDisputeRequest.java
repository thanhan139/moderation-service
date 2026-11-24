package com.coreflows.moderation.api.web.dto;

import com.coreflows.moderation.core.domain.model.DisputeCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RaiseDisputeRequest(
        @NotNull UUID campaignId,
        UUID taskId,
        @NotNull DisputeCategory category,
        @NotBlank String description) {
}