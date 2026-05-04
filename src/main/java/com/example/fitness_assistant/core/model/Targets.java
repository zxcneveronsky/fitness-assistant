package com.example.fitness_assistant.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Targets {
    private Double targetKcal;
    private Double targetProteins;
    private Double targetFats;
    private Double targetCarbs;
    private Boolean useAutopilot;
}

