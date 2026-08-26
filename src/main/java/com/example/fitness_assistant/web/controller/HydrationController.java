package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.hydration.CreateHydrationUseCase;
import com.example.fitness_assistant.application.service.hydration.DeleteHydrationUseCase;
import com.example.fitness_assistant.application.service.hydration.FindHydrationUseCase;
import com.example.fitness_assistant.application.service.hydration.GetDailyHydrationUseCase;
import com.example.fitness_assistant.application.service.hydration.UpdateHydrationUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.request.create.CreateHydrationRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateHydrationRequest;
import com.example.fitness_assistant.web.dto.response.hydration.DailyHydrationResponse;
import com.example.fitness_assistant.web.dto.response.hydration.HydrationResponse;
import com.example.fitness_assistant.web.mapper.DailyHydrationWebMapper;
import com.example.fitness_assistant.web.mapper.HydrationWebMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/hydration")
@RequiredArgsConstructor
@Validated
public class HydrationController {

    private final CreateHydrationUseCase createHydrationUseCase;
    private final FindHydrationUseCase findHydrationUseCase;
    private final UpdateHydrationUseCase updateHydrationUseCase;
    private final DeleteHydrationUseCase deleteHydrationUseCase;
    private final HydrationWebMapper hydrationWebMapper;
    private final GetDailyHydrationUseCase getDailyHydrationUseCase;
    private final DailyHydrationWebMapper dailyHydrationWebMapper;

    @GetMapping("/{id}")
    public HydrationResponse getHydrationById(@AuthenticationPrincipal UserDetailsAdapter adapter, @PathVariable("id") Long hydrationId){
        return hydrationWebMapper.toResponse(findHydrationUseCase.findById(adapter.getUserId(), hydrationId));
    }

    @GetMapping("/search")
    public Page<HydrationResponse> searchHydration(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date,
            @PageableDefault(size = 12) Pageable pageable) {
        return findHydrationUseCase.searchHydration(adapter.getUserId(), date, pageable)
                .map(hydrationWebMapper::toResponse);
    }

    @GetMapping("/daily")
    public DailyHydrationResponse getDailyHydration(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate date) {
        return dailyHydrationWebMapper.toResponse(getDailyHydrationUseCase.getDailyHydration(adapter.getUserId(), date));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HydrationResponse createHydration(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody CreateHydrationRequest request) {
        return hydrationWebMapper.toResponse(
                createHydrationUseCase.createHydration(adapter.getUserId(), hydrationWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public HydrationResponse updateHydration(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @Valid @RequestBody UpdateHydrationRequest request) {
        return hydrationWebMapper.toResponse(
                updateHydrationUseCase.updateHydration(adapter.getUserId(), hydrationWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHydration(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable("id") Long hydrationId) {
        deleteHydrationUseCase.deleteHydration(adapter.getUserId(), hydrationId);
    }
}
