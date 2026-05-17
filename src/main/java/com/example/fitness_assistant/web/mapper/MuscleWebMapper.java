package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.web.dto.response.MuscleResponse;
import org.springframework.stereotype.Component;

@Component
public class MuscleWebMapper {

    public MuscleResponse toResponse(Muscle muscle) {
        return new MuscleResponse(muscle.getId(), muscle.getName());
    }
}
