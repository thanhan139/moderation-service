package com.coreflows.moderation.api.web.dto;

import com.coreflows.moderation.core.domain.model.DisputeDecision;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CloseDisputeRequest(
        @NotNull DisputeDecision decision,
        @NotBlank String resolution) {
}