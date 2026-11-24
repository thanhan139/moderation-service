package com.coreflows.moderation.core.application.port.in;

public record PageQuery(int page, int size) {
    public PageQuery {
        if (page < 0) page = 0;
        if (size < 1) size = 20;
        if (size > 100) size = 100;
    }
}