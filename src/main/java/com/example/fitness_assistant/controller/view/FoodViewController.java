package com.example.fitness_assistant.controller.view;

import com.example.fitness_assistant.dto.FoodSearchDTO;
import com.example.fitness_assistant.service.FoodSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public String showFoodsPage(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20) Pageable pageable,
            Model model) {

        if (name != null && !name.isBlank()) {
            try {
                model.addAttribute("foods", foodSearchService.findFoodByName(name, pageable).getContent());
            } catch (Exception e) {
                model.addAttribute("foods", List.of());
            }
        } else {
            model.addAttribute("foods", List.of());
        }

        return "explore-foods";
    }
}