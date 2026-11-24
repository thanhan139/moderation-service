package com.coreflows.moderation.config;

import com.coreflows.moderation.core.application.port.out.ModerationActionRepositoryPort;
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
}