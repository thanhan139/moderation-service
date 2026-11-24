package com.coreflows.moderation.api.web;

import com.coreflows.moderation.core.application.port.out.ModerationActionRepositoryPort;
import com.coreflows.moderation.core.domain.exception.OperationNotPermittedException;
import com.coreflows.moderation.core.domain.model.ModerationAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/internal/moderation")
public class InternalModerationController {

    private final byte[] internalSecret;
    private final ModerationActionRepositoryPort repository;

    public InternalModerationController(
            @Value("${app.internal.secret}") String internalSecret,
            ModerationActionRepositoryPort repository) {
        if (internalSecret == null || internalSecret.isEmpty()) {
            throw new IllegalStateException(
                    "app.internal.secret (MODERATION_INTERNAL_SECRET) is required for internal endpoints");
        }
        this.internalSecret = internalSecret.getBytes(StandardCharsets.UTF_8);
        this.repository = repository;
    }

    @GetMapping("/targets/{type}/{id}/latest-action")
    public LatestActionResponse latestAction(
            @PathVariable String type,
            @PathVariable UUID id,
            @RequestHeader(value = "X-Internal-Secret", required = false) String secret) {
        assertSecret(secret);
        return repository.findLatestByTarget(
                com.coreflows.moderation.core.domain.model.TargetType.valueOf(type.toUpperCase()), id)
                .map(LatestActionResponse::from)
                .orElse(new LatestActionResponse(null, null, null, null, null));
    }

    public record LatestActionResponse(
            String action,
            String targetType,
            UUID targetId,
            String reason,
            UUID moderatorId) {
        public static LatestActionResponse from(ModerationAction action) {
            return new LatestActionResponse(
                    action.getAction().name(),
                    action.getTargetType().name(),
                    action.getTargetId(),
                    action.getReason(),
                    action.getModeratorId());
        }
    }

    private void assertSecret(String secret) {
        if (secret == null
                || !MessageDigest.isEqual(internalSecret, secret.getBytes(StandardCharsets.UTF_8))) {
            throw new OperationNotPermittedException("Invalid internal secret");
        }
    }
}