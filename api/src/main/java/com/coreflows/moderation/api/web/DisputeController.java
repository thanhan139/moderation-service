package com.coreflows.moderation.api.web;

import com.coreflows.moderation.api.security.TokenRoles;
import com.coreflows.moderation.api.web.dto.CloseDisputeRequest;
import com.coreflows.moderation.api.web.dto.DisputeResponse;
import com.coreflows.moderation.api.web.dto.PageResponse;
import com.coreflows.moderation.api.web.dto.RaiseDisputeRequest;
import com.coreflows.moderation.core.application.port.in.Actor;
import com.coreflows.moderation.core.application.port.in.GetDisputeQuery;
import com.coreflows.moderation.core.application.port.in.ManageDisputeUseCase;
import com.coreflows.moderation.core.application.port.in.PageQuery;
import com.coreflows.moderation.core.domain.model.DisputeStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/disputes")
public class DisputeController {

    private final ManageDisputeUseCase manageDisputeUseCase;
    private final GetDisputeQuery getDisputeQuery;

    public DisputeController(ManageDisputeUseCase manageDisputeUseCase,
                             GetDisputeQuery getDisputeQuery) {
        this.manageDisputeUseCase = manageDisputeUseCase;
        this.getDisputeQuery = getDisputeQuery;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DisputeResponse raiseDispute(@Valid @RequestBody RaiseDisputeRequest request,
                                        @AuthenticationPrincipal Jwt jwt) {
        var actor = new Actor(TokenRoles.userId(jwt), "koc");
        return DisputeResponse.from(manageDisputeUseCase.raiseDispute(actor,
                new ManageDisputeUseCase.RaiseDisputeCommand(
                        request.campaignId(), request.taskId(), request.category(), request.description())));
    }

    @GetMapping("/mine")
    public PageResponse<DisputeResponse> listMine(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var userId = TokenRoles.userId(jwt);
        return PageResponse.from(
                getDisputeQuery.listMine(userId, new PageQuery(page, size))
                        .map(DisputeResponse::from));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public PageResponse<DisputeResponse> listQueue(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        DisputeStatus ds = status == null ? DisputeStatus.OPEN : DisputeStatus.valueOf(status.toUpperCase());
        return PageResponse.from(
                getDisputeQuery.listQueue(ds, new PageQuery(page, size))
                        .map(DisputeResponse::from));
    }

    @GetMapping("/{id}")
    public DisputeResponse getDispute(@PathVariable UUID id) {
        return DisputeResponse.from(getDisputeQuery.getById(id));
    }

    @PostMapping("/{id}/take")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public DisputeResponse takeCase(@PathVariable UUID id,
                                    @AuthenticationPrincipal Jwt jwt) {
        var actor = new Actor(TokenRoles.userId(jwt), TokenRoles.isAdmin(jwt) ? "admin" : "moderator");
        return DisputeResponse.from(manageDisputeUseCase.takeCase(id, actor));
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public DisputeResponse closeDispute(@PathVariable UUID id,
                                        @Valid @RequestBody CloseDisputeRequest request,
                                        @AuthenticationPrincipal Jwt jwt) {
        var actor = new Actor(TokenRoles.userId(jwt), TokenRoles.isAdmin(jwt) ? "admin" : "moderator");
        return DisputeResponse.from(manageDisputeUseCase.closeDispute(id, actor,
                new ManageDisputeUseCase.CloseDisputeCommand(request.decision(), request.resolution())));
    }
}