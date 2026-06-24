package com.example.fitness_assistant.web.dto.response.workoutsession;

import java.util.List;

public record ExerciseSetsResponse(
    Long exerciseId,
    String name,
    List<SetItemResponse> sets
) {}
