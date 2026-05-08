package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.infrastructure.persistence.entity.SetEntity;
import org.springframework.stereotype.Component;

@Component
public class SetMapper {

    public Set toDomain(SetEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Set(
                entity.getId(),
                entity.getSession().getId(),
                entity.getExercise().getId(),
                entity.getWeight(),
                entity.getReps(),
                entity.getCreatedAt()
        );
    }

    public SetEntity toEntity(Set domain) {
        if (domain == null) {
            return null;
        }
        return new SetEntity(
                domain.getId(),
                null, // Это поле проставляется в адаптере через getReferenceById
                null, // Это поле проставляется в адаптере через getReferenceById
                domain.getWeight(),
                domain.getReps(),
                domain.getCreatedAt()
        );
    }
}