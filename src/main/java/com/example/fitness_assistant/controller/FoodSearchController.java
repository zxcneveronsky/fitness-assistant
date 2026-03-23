package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.FoodCreateDTO;
import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.service.FoodSearchService;
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
@RequestMapping("/api/v1/food")
@Slf4j
@Tag(name = "Продукты питания", description = "Поиск КБЖУ продуктов и управление базой питания")
public class FoodSearchController {

    private final FoodSearchService foodSearchService;

    public FoodSearchController(FoodSearchService foodSearchService) {
        this.foodSearchService = foodSearchService;
    }

    @Operation(summary = "Поиск продуктов по названию",
            description = "Ищет продукты по вхождению строки в название. Регистр не важен.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список найденных продуктов"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден",
                    content = @Content(schema = @Schema(example = "{\"error\": \"Гречка не найдено.\"}")))
    })
    @GetMapping
    public Page<FoodSearchDTO> getFoodByName(
            @Parameter(description = "Название продукта", example = "Гречка")
            @RequestParam String name,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Поиск продукта по названию: '{}'", name);
        return foodSearchService.findFoodByName(name, pageable);
    }

    @Operation(summary = "Добавить продукт",
            description = "Создаёт новый продукт. КБЖУ на 100г. Требует авторизации.",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Продукт добавлен"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FoodSearchDTO addFood(@Valid @RequestBody FoodCreateDTO dto) {
        log.info("Добавление продукта: {}", dto.name());
        return foodSearchService.addFood(dto);
    }

    @Operation(summary = "Обновить продукт",
            description = "Обновляет все поля продукта по ID. Требует авторизации.",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Продукт обновлён"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
    @PutMapping("/{id}")
    public FoodSearchDTO updateFood(
            @Parameter(description = "ID продукта", example = "42")
            @PathVariable Long id,
            @Valid @RequestBody FoodCreateDTO dto) {
        log.info("Обновление продукта с id: {}", id);
        return foodSearchService.updateFood(id, dto);
    }

    @Operation(summary = "Удалить продукт",
            description = "Удаляет продукт по ID. Требует авторизации.",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Продукт удалён"),
            @ApiResponse(responseCode = "404", description = "Продукт не найден"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(
            @Parameter(description = "ID продукта", example = "42")
            @PathVariable Long id) {
        log.warn("Удаление продукта с id: {}", id);
        foodSearchService.deleteFood(id);
    }
}