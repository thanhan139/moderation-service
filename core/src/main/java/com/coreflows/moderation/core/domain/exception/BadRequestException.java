package com.coreflows.moderation.core.domain.exception;

public class BadRequestException extends DomainException {
    public BadRequestException(String message) {
        super(message);
    }
}