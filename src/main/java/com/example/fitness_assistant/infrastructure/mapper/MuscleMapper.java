package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.infrastructure.persistence.entity.MuscleEntity;
import org.springframework.stereotype.Component;

@Component
public class MuscleMapper {

    public Muscle toDomain(MuscleEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Muscle(
                entity.getId(),
                entity.getName()
        );
    }

    public MuscleEntity toEntity(Muscle domain) {
        if (domain == null) {
            return null;
        }
        return new MuscleEntity(
                domain.getId(),
                domain.getName()
        );
    }
}