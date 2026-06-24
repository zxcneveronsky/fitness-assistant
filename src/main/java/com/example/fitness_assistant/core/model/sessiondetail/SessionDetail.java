package com.example.fitness_assistant.core.model.sessiondetail;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class SessionDetail {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<ExerciseDetail> exercises;
}
