package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.set.*;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/workout/session/set")
@RequiredArgsConstructor
public class SetController {
    private final CreateSetUseCase createSetUseCase;
    private final FindSetUseCase findSetUseCase;
    private final GetAllSetsUseCase getAllSetsUseCase;
    private final UpdateSetUseCase updateSetUseCase;
    private final DeleteSetUseCase deleteSetUseCase;
    private final SetWebMapper setWebMapper;

    @GetMapping
    public Page<SetResponse> getAllSets(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long sessionId,
            @RequestParam Long exerciseId,
            @PageableDefault(size = 9) Pageable pageable) {
        return getAllSetsUseCase.getAllSets(sessionId, exerciseId, userDetails, pageable)
                .map(setWebMapper::toResponse);
    }

    @GetMapping("/{id}")
    public SetResponse getSetById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestParam Long sessionId) {
        return setWebMapper.toResponse(
                findSetUseCase.findById(id, sessionId, userDetails)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SetResponse createSet(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateSetRequest request) {
        return setWebMapper.toResponse(
                createSetUseCase.createSet(userDetails, setWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public SetResponse updateSet(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateSetRequest request) {
        return setWebMapper.toResponse(
                updateSetUseCase.updateSet(userDetails, setWebMapper.toDomain(request))
        );
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSet(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestParam Long sessionId) {
        deleteSetUseCase.deleteSet(id, sessionId, userDetails);
    }

}
