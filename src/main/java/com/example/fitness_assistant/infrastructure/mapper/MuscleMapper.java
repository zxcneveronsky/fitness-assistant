package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.infrastructure.persistence.entity.MuscleEntity;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Component
public class MuscleMapper {

    public Muscle toDomain(MuscleEntity entity) {
        if (!Hibernate.isInitialized(entity)){
            return new Muscle(entity.getId(),null);
        }
        return new Muscle(
                entity.getId()  ,
                entity.getName()
        );
    }

    public MuscleEntity toEntity(Muscle domain) {
        return new MuscleEntity(
                domain.getId(),
                domain.getName()
        );
    }
}