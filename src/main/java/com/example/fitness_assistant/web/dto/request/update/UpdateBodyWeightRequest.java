package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record UpdateBodyWeightRequest(
        @NotNull(message = "ID записи не может быть пустым")
        Long id,

        @Min(value = 5, message = "Вес должен быть не менее 5 кг")
        @Max(value = 500, message = "Вес должен быть не более 500 кг")
        Double weightKg,

        @PastOrPresent(message = "Дата не может быть в будущем")
        LocalDate measuredAt
) {}
