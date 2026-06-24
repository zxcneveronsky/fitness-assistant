package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.targets.FindTargetsUseCase;
import com.example.fitness_assistant.application.service.targets.UpdateTargetsUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.update.UpdateTargetsRequest;
import com.example.fitness_assistant.web.dto.response.targets.TargetStatusResponse;
import com.example.fitness_assistant.web.dto.response.targets.TargetsResponse;
import com.example.fitness_assistant.web.mapper.TargetsWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Validated
public class TargetsController {

    private final UpdateTargetsUseCase updateTargetsUseCase;
    private final FindTargetsUseCase findTargetsUseCase;
    private final TargetsWebMapper targetsWebMapper;

    @PatchMapping("/targets")
    @ResponseStatus(HttpStatus.OK)
    public TargetsResponse updateTargets(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody UpdateTargetsRequest request) {
        return targetsWebMapper.toTargetsResponse(
                updateTargetsUseCase.updateTargets(adapter.getUserId(), targetsWebMapper.toDomain(request))
        );
    }

    @GetMapping("/targets")
    public TargetsResponse getTarget(@AuthenticationPrincipal UserDetailsAdapter adapter) {
        return targetsWebMapper.toTargetsResponse(findTargetsUseCase.findById(adapter.getUserId()));
    }

    @PatchMapping("/targets/status")
    @ResponseStatus(HttpStatus.OK)
    public TargetStatusResponse updateAutopilotStatus(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam boolean enabled) {
        return targetsWebMapper.toTargetStatusResponse(
                updateTargetsUseCase.updateAutopilotStatus(adapter.getUserId(), enabled)
        );
    }
}
