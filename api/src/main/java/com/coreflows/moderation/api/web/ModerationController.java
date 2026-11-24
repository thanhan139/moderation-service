package com.coreflows.moderation.api.web;

import com.coreflows.moderation.api.security.TokenRoles;
import com.coreflows.moderation.api.web.dto.ActionResponse;
import com.coreflows.moderation.api.web.dto.PageResponse;
import com.coreflows.moderation.api.web.dto.RaiseActionRequest;
import com.coreflows.moderation.core.application.port.in.Actor;
import com.coreflows.moderation.core.application.port.in.GetModerationQuery;
import com.coreflows.moderation.core.application.port.in.ModerateUserUseCase;
import com.coreflows.moderation.core.application.port.in.PageQuery;
import com.coreflows.moderation.core.domain.model.ActionType;
import com.coreflows.moderation.core.domain.model.TargetType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moderation")
public class ModerationController {

    private final ModerateUserUseCase moderateUserUseCase;
    private final GetModerationQuery getModerationQuery;

    public ModerationController(ModerateUserUseCase moderateUserUseCase,
                                GetModerationQuery getModerationQuery) {
        this.moderateUserUseCase = moderateUserUseCase;
        this.getModerationQuery = getModerationQuery;
    }

    @PostMapping("/actions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public ActionResponse raiseAction(@Valid @RequestBody RaiseActionRequest request,
                                      @AuthenticationPrincipal Jwt jwt) {
        return ActionResponse.from(moderateUserUseCase.raiseAction(
                new Actor(TokenRoles.userId(jwt), TokenRoles.isAdmin(jwt) ? "admin" : "moderator"),
                new ModerateUserUseCase.RaiseActionCommand(
                        request.targetTypeEnum(), request.targetId(), request.actionEnum(), request.reason())));
    }

    @GetMapping("/actions")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public PageResponse<ActionResponse> listActions(
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) UUID targetId,
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        TargetType tt = targetType == null ? null : TargetType.valueOf(targetType);
        ActionType at = action == null ? null : ActionType.valueOf(action);
        return PageResponse.from(
                getModerationQuery.list(tt, targetId, at, new PageQuery(page, size))
                        .map(ActionResponse::from));
    }

    @GetMapping("/actions/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ActionResponse getAction(@PathVariable UUID id) {
        return ActionResponse.from(getModerationQuery.getById(id));
    }
}