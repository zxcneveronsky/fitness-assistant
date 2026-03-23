package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exercises")
@Slf4j
@Tag(name = "Упражнения", description = "Поиск и управление базой упражнений")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Operation(
            summary = "Все упражнения с пагинацией",
            description = "Возвращает страницу упражнений. По умолчанию 10 на страницу, сортировка по имени."
    )
    @ApiResponse(responseCode = "200", description = "Список упражнений")
    @GetMapping
    public Page<ExerciseDTO> getAllExercisePaged(
            @Parameter(description = "Параметры пагинации: page, size, sort")
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Запрос на пагинацию: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return exerciseService.getAllExercisePaged(pageable);
    }

    @Operation(
            summary = "Упражнения по группе мышц",
            description = "Ищет упражнения где указанная мышца является основной или вспомогательной. Регистр не важен."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Найденные упражнения"),
            @ApiResponse(responseCode = "404", description = "Упражнения для данной мышцы не найдены",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Грудь не найдено.\"}")))
    })
    @GetMapping("/muscle/{muscle}")
    public Page<ExerciseDTO> getExerciseByMuscle(
            @Parameter(description = "Название мышцы или группы мышц", example = "Грудь")
            @PathVariable String muscle,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Поиск упражнений для группы мышц: {}", muscle);
        return exerciseService.getExercisesByMuscle(muscle, pageable);
    }

    @Operation(
            summary = "Упражнение по названию",
            description = "Возвращает полную информацию об упражнении включая все задействованные мышцы."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Упражнение найдено"),
            @ApiResponse(responseCode = "404", description = "Упражнение не найдено",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Жим лёжа не найдено.\"}")))
    })
    @GetMapping("/exercise/{exercise}")
    public ExerciseDTO getExerciseByName(
            @Parameter(description = "Точное название упражнения", example = "Жим лёжа")
            @PathVariable String exercise) {
        log.info("Поиск информации по упражнению: {}", exercise);
        return exerciseService.getExerciseByName(exercise);
    }

    @Operation(
            summary = "Поиск упражнений по названию или мышце",
            description = "Сначала ищет по мышце, затем по вхождению в название. Удобен для строки поиска."
    )
    @ApiResponse(responseCode = "200", description = "Результаты поиска")
    @GetMapping("/search")
    public Page<ExerciseDTO> searchExercises(
            @Parameter(description = "Поисковый запрос", example = "жим")
            @RequestParam String name,
            @PageableDefault(size = 9) Pageable pageable) {
        log.info("Поиск упражнений по запросу: '{}'", name);
        return exerciseService.searchExercisesByName(name, pageable);
    }

    @Operation(
            summary = "Добавить упражнение",
            description = "Создаёт новое упражнение с привязкой к группам мышц. Требует авторизации.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Упражнение создано"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciseDTO addExercise(@Valid @RequestBody ExerciseDTO exercise) {
        return exerciseService.addExercise(exercise);
    }

    @Operation(
            summary = "Удалить упражнение",
            description = "Удаляет упражнение и все связанные записи о мышцах. Требует авторизации.",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Упражнение удалено"),
            @ApiResponse(responseCode = "404", description = "Упражнение не найдено"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
    @DeleteMapping("/{exerciseName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExercise(
            @Parameter(description = "Название упражнения для удаления", example = "Жим лёжа")
            @PathVariable String exerciseName) {
        log.warn("Удаление упражнения: {}", exerciseName);
        exerciseService.deleteExercise(exerciseName);
    }
}