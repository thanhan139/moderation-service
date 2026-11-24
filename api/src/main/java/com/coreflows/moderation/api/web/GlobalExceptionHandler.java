package com.coreflows.moderation.api.web;

import com.coreflows.moderation.core.domain.exception.BadRequestException;
import com.coreflows.moderation.core.domain.exception.DomainException;
import com.coreflows.moderation.core.domain.exception.ModerationActionNotFoundException;
import com.coreflows.moderation.core.domain.exception.OperationNotPermittedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    public ProblemDetail handleNotPermitted(OperationNotPermittedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(ModerationActionNotFoundException.class)
    public ProblemDetail handleNotFound(ModerationActionNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ProblemDetail handleDomain(DomainException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }
}