package com.example.fitness_assistant.controller.view;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.service.FoodSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/explore/food")
@RequiredArgsConstructor
public class FoodViewController {

    private final FoodSearchService foodSearchService;

    @GetMapping
    public String showFoodsPage(@RequestParam(required = false) String name, Model model) {
        List<FoodSearchDTO> foods;
        if (name != null && !name.isEmpty()) {
            try {
                foods = foodSearchService.findFoodByName(name);
            } catch (Exception e) {
                foods = List.of();
            }
        } else {
            foods = List.of(); // Сервер спит, база отдыхает
        }
        model.addAttribute("foods", foods);
        return "explore-foods";
    }
}