package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.hydration.*;
import com.example.fitness_assistant.web.dto.request.create.CreateHydrationRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateHydrationRequest;
import com.example.fitness_assistant.web.dto.response.hydration.DailyHydrationResponse;
import com.example.fitness_assistant.web.dto.response.hydration.HydrationResponse;
import com.example.fitness_assistant.web.mapper.DailyHydrationWebMapper;
import com.example.fitness_assistant.web.mapper.HydrationWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/hydration")
@RequiredArgsConstructor
public class HydrationController {

    private final CreateHydrationUseCase createHydrationUseCase;
    private final FindHydrationUseCase findHydrationUseCase;
    private final UpdateHydrationUseCase updateHydrationUseCase;
    private final DeleteHydrationUseCase deleteHydrationUseCase;
    private final HydrationWebMapper hydrationWebMapper;
    private final GetDailyHydrationUseCase getDailyHydrationUseCase;
    private final DailyHydrationWebMapper dailyHydrationWebMapper;

    @GetMapping("/{id}")
    public HydrationResponse getHydrationById(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long id){
        return hydrationWebMapper.toResponse(findHydrationUseCase.findById(id,userDetails));
    }



    @GetMapping("/search")
    public Page<HydrationResponse> searchHydration(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime,
            @PageableDefault(size = 9) Pageable pageable) {
        return findHydrationUseCase.findHydration(localDateTime, userDetails, pageable)
                .map(hydrationWebMapper::toResponse);
    }

    @GetMapping("/daily")
    public DailyHydrationResponse getDailyHydration(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
        return dailyHydrationWebMapper.toResponse(getDailyHydrationUseCase.getDailyHydration(localDateTime, userDetails));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HydrationResponse createHydration(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateHydrationRequest request) {
        return hydrationWebMapper.toResponse(
                createHydrationUseCase.createHydration(userDetails, hydrationWebMapper.toDomain(request))
        );
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public HydrationResponse updateHydration(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateHydrationRequest request) {
        return hydrationWebMapper.toResponse(
                updateHydrationUseCase.updateHydration(userDetails, hydrationWebMapper.toDomain(request))
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHydration(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        deleteHydrationUseCase.deleteHydration(id, userDetails);
    }
}
