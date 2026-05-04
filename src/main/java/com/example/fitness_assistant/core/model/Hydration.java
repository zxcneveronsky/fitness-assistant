package com.example.fitness_assistant.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hydration {
    private Long id;
    private Long userId;
    private String name;
    private Double amount;
    private LocalDateTime consumedAt;
}
