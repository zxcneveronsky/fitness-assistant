package com.example.fitness_assistant.core.model.sessiondetail;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SetDetail {
    private Long id;
    private Long exerciseId;
    private String name;
    private Double weight;
    private Integer reps;
    private LocalDateTime createdAt;
}
