package com.coreflows.moderation.config;

import com.coreflows.moderation.core.application.port.out.DisputeRepositoryPort;
import com.coreflows.moderation.core.application.port.out.ModerationActionRepositoryPort;
import com.coreflows.moderation.core.application.service.DisputeService;
import com.coreflows.moderation.core.application.service.ModerationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class UseCaseConfig {

    @Bean
    public ModerationService moderationService(ModerationActionRepositoryPort repository) {
        return new ModerationService(repository, Clock.systemUTC());
    }

    @Bean
    public DisputeService disputeService(DisputeRepositoryPort repository) {
        return new DisputeService(repository, Clock.systemUTC());
    }
}