package com.example.fitness_assistant.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ExerciseHistoryPoint {
    private Long sessionId;
    private String workoutName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<Set> sets;
}
