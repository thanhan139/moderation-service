package com.coreflows.moderation.core.domain.exception;

public class OperationNotPermittedException extends DomainException {
    public OperationNotPermittedException(String message) {
        super(message);
    }
}