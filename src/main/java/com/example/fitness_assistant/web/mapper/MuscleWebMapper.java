package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.web.dto.request.create.CreateMuscleRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateMuscleRequest;
import com.example.fitness_assistant.web.dto.response.MuscleResponse;
import org.springframework.stereotype.Component;

@Component
public class MuscleWebMapper {

    public Muscle toDomain(CreateMuscleRequest request) {
        return new Muscle(
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.name()
        );
    }

    public Muscle toDomain(UpdateMuscleRequest request) {
        return new Muscle(
                request.id(),
                request.name()
        );
    }

    public MuscleResponse toResponse(Muscle muscle) {
        return new MuscleResponse(muscle.getId(), muscle.getName());
    }
}
