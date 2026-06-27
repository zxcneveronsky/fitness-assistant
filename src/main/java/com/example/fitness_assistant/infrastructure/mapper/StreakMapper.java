package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.Streak;
import com.example.fitness_assistant.infrastructure.persistence.entity.StreakEntity;
import org.springframework.stereotype.Component;

@Component
public class StreakMapper {

    public Streak toDomain(StreakEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Streak(
                entity.getId(),
                entity.getStreak(),
                entity.getLastVisitDate()
        );
    }

    public StreakEntity toEntity(Streak domain) {
        if (domain == null) {
            return null;
        }
        return new StreakEntity(
                domain.getUserId(),
                null, // Это поле проставляется в адаптере через getReferenceById
                domain.getStreak(),
                domain.getLastVisitDate()
        );
    }
}
