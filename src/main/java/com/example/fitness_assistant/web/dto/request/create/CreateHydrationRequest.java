package com.example.fitness_assistant.web.dto.request.create;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record CreateHydrationRequest(
        @NotNull(message = "Название напитка не может быть пустым")
        @NotBlank(message = "Название напитка не может быть пустым")
        @Size(max = 255, message = "Название слишком длинное")
        String name,

        @NotNull(message = "Количество не может быть пустым")
        @Positive(message = "Количество должно быть положительным")
        @Max(value = 10, message = "Количество не может быть больше 10 литров")
        Double amount,

        @NotNull(message = "Дата приема должна быть указана")
        @PastOrPresent(message = "Дата приема не может быть в будущем")
        LocalDateTime consumedAt
) {}
