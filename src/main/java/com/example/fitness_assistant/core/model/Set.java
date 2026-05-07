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
public class Set {
    private Long id;
    private Long sessionId;
    private Long exerciseId;
    private Long weight;
    private Integer reps;
    private LocalDateTime createdAt;
}
