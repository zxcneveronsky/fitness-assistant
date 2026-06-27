package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.BodyWeight;
import com.example.fitness_assistant.infrastructure.persistence.entity.BodyWeightEntity;
import org.springframework.stereotype.Component;

@Component
public class BodyWeightMapper {

    public BodyWeight toDomain(BodyWeightEntity entity) {
        if (entity == null) {
            return null;
        }
        return new BodyWeight(
                entity.getId(),
                entity.getUser().getId(),
                entity.getWeight(),
                entity.getMeasuredAt()
        );
    }

    public BodyWeightEntity toEntity(BodyWeight domain) {
        if (domain == null) {
            return null;
        }
        return new BodyWeightEntity(
                domain.getId(),
                null, // Это поле проставляется в адаптере через getReferenceById
                domain.getWeight(),
                domain.getMeasuredAt()
        );
    }
}
