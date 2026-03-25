package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.FoodCreateDTO;
import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.service.FoodSearchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/food")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FoodSearchController {

    private final FoodSearchService foodSearchService;

    @GetMapping
    public Page<FoodSearchDTO> getFoodByName(@RequestParam
            @NotBlank(message = "Запрос не может быть пустым")
            @Size(min = 2, max = 50, message = "Длина запроса от 2 до 50 символов")
            String name,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Поиск продукта по названию: '{}'", name);
        return foodSearchService.findFoodByName(name, pageable);
    }

    @GetMapping("/{id}")
    public FoodSearchDTO getFoodById(@PathVariable Long id) {
        return foodSearchService.getFoodById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FoodSearchDTO addFood(@Valid @RequestBody FoodCreateDTO dto) {
        log.info("Добавление продукта: {}", dto.name());
        return foodSearchService.addFood(dto);
    }

    @PutMapping("/{id}")
    public FoodSearchDTO updateFood(@PathVariable Long id, @Valid @RequestBody FoodCreateDTO dto) {
        log.info("Обновление продукта с id: {}", id);
        return foodSearchService.updateFood(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable Long id) {
        log.warn("Удаление продукта с id: {}", id);
        foodSearchService.deleteFood(id);
    }
}