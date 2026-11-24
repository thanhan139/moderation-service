package com.coreflows.moderation.core.domain.exception;

public class ModerationActionNotFoundException extends DomainException {
    public ModerationActionNotFoundException(String message) {
        super(message);
    }
}