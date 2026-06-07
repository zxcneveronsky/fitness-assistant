package com.example.fitness_assistant.web.dto.response;

import java.time.LocalDate;

public record BodyWeightResponse(
        Long id,
        Double weightKg,
        LocalDate measuredAt
) {}
