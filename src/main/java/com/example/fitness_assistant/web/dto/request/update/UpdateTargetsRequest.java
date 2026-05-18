package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.PositiveOrZero;

public record UpdateTargetsRequest(
        @PositiveOrZero(message = "Калории не могут быть отрицательными")
        Double targetKcal,
        @PositiveOrZero(message = "Белки не могут быть отрицательными")
        Double targetProteins,
        @PositiveOrZero(message = "Жиры не могут быть отрицательными")
        Double targetFats,
        @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
        Double targetCarbs,
        @PositiveOrZero(message = "Воды не могут быть отрицательными")
        Double targetHydration
) {}

