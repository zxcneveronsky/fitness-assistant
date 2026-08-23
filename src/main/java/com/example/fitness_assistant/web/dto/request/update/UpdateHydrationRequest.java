package com.example.fitness_assistant.web.dto.request.update;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record UpdateHydrationRequest(
        @NotNull(message = "ID записи не может быть пустым")
        Long id,

        @Size(max = 255, message = "Название напитка слишком длинное")
        String name,

        @Positive(message = "Количество должно быть положительным")
        @Max(value = 10, message = "Количество не может быть больше 10 литров")
        Double amount,

        @PastOrPresent(message = "Дата приема не может быть в будущем")
        LocalDateTime consumedAt
) {}
