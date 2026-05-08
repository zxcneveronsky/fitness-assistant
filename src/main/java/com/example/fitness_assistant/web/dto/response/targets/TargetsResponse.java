package com.example.fitness_assistant.web.dto.response.targets;


public record TargetsResponse(
        Double targetKcal,
        Double targetProteins,
        Double targetFats,
        Double targetCarbs,
        Double targetHydration
) {}
