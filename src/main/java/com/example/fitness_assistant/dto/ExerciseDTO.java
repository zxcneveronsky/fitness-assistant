package com.example.fitness_assistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Упражнение с привязкой к мышцам")
public record ExerciseDTO(

        @Schema(description = "Название упражнения", example = "Жим лёжа")
        @NotBlank(message = "Название не может быть пустым")
        String exerciseName,

        @Schema(description = "Описание техники выполнения", example = "Лечь на скамью, взять штангу хватом чуть шире плеч...")
        String description,

        @Schema(description = "Список задействованных мышц")
        @NotNull(message = "Список мышц не может не быть")
        @NotEmpty(message = "Список мышц не может быть пустым")
        @Valid
        List<MuscleDTO> muscles
) {
    @Schema(description = "Мышца задействованная в упражнении")
    public record MuscleDTO(

            @Schema(description = "Основная группа мышц", example = "Грудь")
            @NotBlank(message = "Название главной мышцы не может быть пустым")
            String muscleGroup,

            @Schema(description = "Конкретная мышца внутри группы", example = "Большая грудная")
            String muscleDetail
    ) {}
}