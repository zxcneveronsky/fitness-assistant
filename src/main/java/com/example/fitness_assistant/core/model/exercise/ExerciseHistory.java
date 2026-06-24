package com.example.fitness_assistant.core.model.exercise;

import com.example.fitness_assistant.core.model.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExerciseHistory {
    private Long sessionId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Set> sets;
}
