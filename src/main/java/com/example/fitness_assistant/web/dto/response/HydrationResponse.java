package com.example.fitness_assistant.web.dto.response;

import java.time.LocalDateTime;

public record HydrationResponse(
        Long id,
        String name,
        Double amount,
        LocalDateTime consumedAt
) {}
