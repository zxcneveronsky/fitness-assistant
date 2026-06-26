package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.bodyweight.CreateBodyWeightUseCase;
import com.example.fitness_assistant.application.service.bodyweight.DeleteBodyWeightUseCase;
import com.example.fitness_assistant.application.service.bodyweight.FindBodyWeightUseCase;
import com.example.fitness_assistant.application.service.bodyweight.UpdateBodyWeightUseCase;
import com.example.fitness_assistant.core.exception.BodyWeightNotFoundException;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.create.CreateBodyWeightRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateBodyWeightRequest;
import com.example.fitness_assistant.web.dto.response.BodyWeightResponse;
import com.example.fitness_assistant.web.mapper.BodyWeightWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profile/weight")
@RequiredArgsConstructor
@Validated
public class BodyWeightController {

    private final CreateBodyWeightUseCase createBodyWeightUseCase;
    private final FindBodyWeightUseCase findBodyWeightUseCase;
    private final UpdateBodyWeightUseCase updateBodyWeightUseCase;
    private final DeleteBodyWeightUseCase deleteBodyWeightUseCase;
    private final BodyWeightWebMapper bodyWeightWebMapper;

    @GetMapping("/{id}")
    public BodyWeightResponse getBodyWeightById(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable("id") Long bodyWeightId) {
        return bodyWeightWebMapper.toResponse(
                findBodyWeightUseCase.findById(adapter.getUserId(), bodyWeightId)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BodyWeightResponse createBodyWeight(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody CreateBodyWeightRequest request) {
        return bodyWeightWebMapper.toResponse(
                createBodyWeightUseCase.createBodyWeight(adapter.getUserId(), bodyWeightWebMapper.toDomain(request))
        );
    }

    @GetMapping
    public List<BodyWeightResponse> getBodyWeightByDateRange(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return findBodyWeightUseCase.findByDateRange(adapter.getUserId(), from, to)
                .stream()
                .map(bodyWeightWebMapper::toResponse)
                .toList();
    }

    @GetMapping("/latest")
    public BodyWeightResponse getLatestBodyWeight(@AuthenticationPrincipal UserDetailsAdapter adapter) {
        return findBodyWeightUseCase.findLatest(adapter.getUserId())
                .map(bodyWeightWebMapper::toResponse)
                .orElseThrow(() -> new BodyWeightNotFoundException(adapter.getUserId()));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public BodyWeightResponse updateBodyWeight(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody UpdateBodyWeightRequest request) {
        return bodyWeightWebMapper.toResponse(
                updateBodyWeightUseCase.updateBodyWeight(adapter.getUserId(), bodyWeightWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBodyWeight(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable("id") Long bodyWeightId) {
        deleteBodyWeightUseCase.deleteBodyWeight(adapter.getUserId(), bodyWeightId);
    }
}
