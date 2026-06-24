package com.example.fitness_assistant.core.model.sessiondetail;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ExerciseDetail {
    private Long exerciseId;
    private String exerciseName;
    private List<SetDetail> sets;
}
