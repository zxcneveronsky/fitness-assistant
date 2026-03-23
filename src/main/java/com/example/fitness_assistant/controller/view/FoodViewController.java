package com.example.fitness_assistant.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/explore/food")
public class FoodViewController {

    @GetMapping
    public String showFoodsPage() {
        return "explore-foods";
    }
}