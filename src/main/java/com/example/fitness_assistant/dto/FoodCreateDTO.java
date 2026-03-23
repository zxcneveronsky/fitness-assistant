package com.example.fitness_assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Данные для создания или обновления продукта питания")
public record FoodCreateDTO(

        @Schema(description = "Название продукта", example = "Гречка варёная")
        @NotBlank(message = "Название продукта не может быть пустым")
        String name,

        @Schema(description = "Производитель или бренд", example = "Увелка")
        String brands,

        @Schema(description = "Калорийность на 100г", example = "92.0")
        @PositiveOrZero(message = "Калории не могут быть отрицательными")
        double kcal,

        @Schema(description = "Белки на 100г в граммах", example = "3.5")
        @PositiveOrZero(message = "Белки не могут быть отрицательными")
        double proteins,

        @Schema(description = "Жиры на 100г в граммах", example = "0.6")
        @PositiveOrZero(message = "Жиры не могут быть отрицательными")
        double fats,

        @Schema(description = "Углеводы на 100г в граммах", example = "19.9")
        @PositiveOrZero(message = "Углеводы не могут быть отрицательными")
        double carbs
) {}