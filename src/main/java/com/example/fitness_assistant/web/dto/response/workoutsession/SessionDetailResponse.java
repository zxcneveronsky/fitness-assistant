package com.example.fitness_assistant.web.dto.response.workoutsession;

import java.time.LocalDateTime;
import java.util.List;

public record SessionDetailResponse(
    Long id,
    LocalDateTime startTime,
    LocalDateTime endTime,
    List<ExerciseSetsResponse> exercises
) {}
