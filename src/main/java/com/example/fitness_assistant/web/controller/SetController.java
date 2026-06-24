package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.set.CreateSetUseCase;
import com.example.fitness_assistant.application.service.set.DeleteSetUseCase;
import com.example.fitness_assistant.application.service.set.FindSetUseCase;
import com.example.fitness_assistant.application.service.set.UpdateSetUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.create.CreateSetRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateSetRequest;
import com.example.fitness_assistant.web.dto.response.SetResponse;
import com.example.fitness_assistant.web.mapper.SetWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workout/session/set")
@RequiredArgsConstructor
@Validated
public class SetController {
    private final CreateSetUseCase createSetUseCase;
    private final FindSetUseCase findSetUseCase;
    private final UpdateSetUseCase updateSetUseCase;
    private final DeleteSetUseCase deleteSetUseCase;
    private final SetWebMapper setWebMapper;

    @GetMapping
    public Page<SetResponse> getAllSets(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam Long sessionId,
            @RequestParam Long exerciseId,
            @PageableDefault(size = 12) Pageable pageable) {
        return findSetUseCase.findAll(sessionId, exerciseId, adapter.getUserId(), pageable)
                .map(setWebMapper::toResponse);
    }

    @GetMapping("/{id}")
    public SetResponse getSetById(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable Long id,
            @RequestParam Long sessionId) {
        return setWebMapper.toResponse(
                findSetUseCase.findById(id, sessionId, adapter.getUserId())
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SetResponse createSet(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody CreateSetRequest request) {
        return setWebMapper.toResponse(
                createSetUseCase.createSet(adapter.getUserId(), setWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public SetResponse updateSet(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody UpdateSetRequest request) {
        return setWebMapper.toResponse(
                updateSetUseCase.updateSet(adapter.getUserId(), setWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSet(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable Long id,
            @RequestParam Long sessionId) {
        deleteSetUseCase.deleteSet(id, sessionId, adapter.getUserId());
    }

}
