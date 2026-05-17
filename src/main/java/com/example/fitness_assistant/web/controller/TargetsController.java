package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.profile.UpdateUserProfileUseCase;
import com.example.fitness_assistant.application.service.targets.FindTargetsUseCase;
import com.example.fitness_assistant.application.service.targets.UpdateTargetsUseCase;
import com.example.fitness_assistant.web.dto.request.update.UpdateTargetsRequest;
import com.example.fitness_assistant.web.dto.response.targets.TargetStatusResponse;
import com.example.fitness_assistant.web.dto.response.targets.TargetsResponse;
import com.example.fitness_assistant.web.mapper.TargetsWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @PatchMapping("/targets")
    @ResponseStatus(HttpStatus.OK)
    public TargetsResponse updateTargets(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateTargetsRequest request) {
        return targetsWebMapper.toTargetsResponse(
                updateTargetsUseCase.updateTargets(userDetails, targetsWebMapper.toDomain(request))
        );
    }

    @GetMapping("/targets")
    public TargetsResponse getTarget(@AuthenticationPrincipal UserDetails userDetails) {
        return targetsWebMapper.toTargetsResponse(findTargetsUseCase.findTargets(userDetails));
    }

    @PatchMapping("/targets/status")
    @ResponseStatus(HttpStatus.OK)
    public TargetStatusResponse updateAutopilotStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam boolean enabled) {
        return targetsWebMapper.toTargetStatusResponse(
                updateUserProfileUseCase.updateAutopilotStatus(userDetails, enabled)
        );
    }
}
