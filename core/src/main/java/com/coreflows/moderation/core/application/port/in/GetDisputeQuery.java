package com.coreflows.moderation.core.application.port.in;

import com.coreflows.moderation.core.domain.model.Dispute;
import com.coreflows.moderation.core.domain.model.DisputeStatus;

import java.util.UUID;

public interface GetDisputeQuery {

    PageResult<Dispute> listQueue(DisputeStatus status, PageQuery page);

    PageResult<Dispute> listMine(UUID userId, PageQuery page);

    Dispute getById(UUID id);
}