package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.web.dto.response.WorkoutSessionResponse;
import org.springframework.stereotype.Component;

@Component
public class WorkoutSessionWebMapper {

    public WorkoutSessionResponse toResponse(WorkoutSession domain) {
        return new WorkoutSessionResponse(
                domain.getId(),
                domain.getWorkoutId(),
                domain.getStartTime(),
                domain.getEndTime()
        );
    }
}