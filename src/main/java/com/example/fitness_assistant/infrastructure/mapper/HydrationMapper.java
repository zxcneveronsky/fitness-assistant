package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.Hydration;
import com.example.fitness_assistant.infrastructure.persistence.entity.HydrationEntity;
import org.springframework.stereotype.Component;

@Component
public class HydrationMapper {

    public Hydration toDomain(HydrationEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Hydration(
                entity.getId(),
                entity.getUser().getId(),
                entity.getName(),
                entity.getAmount(),
                entity.getConsumedAt()
        );
    }

    public HydrationEntity toEntity(Hydration domain) {
        if (domain == null) {
            return null;
        }
        return new HydrationEntity(
                domain.getId(),
                null, // Это поле проставляется в адаптере через GetReferenceById
                domain.getName(),
                domain.getAmount(),
                domain.getConsumedAt()
        );
    }
}
