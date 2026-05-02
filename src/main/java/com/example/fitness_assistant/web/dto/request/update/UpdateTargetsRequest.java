package com.example.fitness_assistant.web.dto.request.update;

public record UpdateTargetsRequest(
        Double targetKcal,
        Double targetProteins,
        Double targetFats,
        Double targetCarbs
) {}

