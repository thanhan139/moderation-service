package com.coreflows.moderation.api.web.dto;

import java.util.List;

public record PageResponse<T>(List<T> items, int page, int size, long totalElements) {

    public static <T> PageResponse<T> from(com.coreflows.moderation.core.application.port.in.PageResult<T> result) {
        return new PageResponse<>(result.items(), result.page(), result.size(), result.totalElements());
    }
}