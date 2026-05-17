package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.infrastructure.persistence.entity.TargetsEntity;
import org.springframework.stereotype.Component;

@Component
public class TargetsMapper {

    public Targets toDomain(TargetsEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Targets(
                entity.getId(),
                entity.getTargetKcal(),
                entity.getTargetProteins(),
                entity.getTargetFats(),
                entity.getTargetCarbs(),
                entity.getTargetHydration(),
                entity.getUseAutopilot()
        );
    }

    public TargetsEntity toEntity(Targets domain) {
        if (domain == null) {
            return null;
        }
        return new TargetsEntity(
                domain.getProfileId(),
                null, // Это поле проставляется в адаптере через getReference
                domain.getTargetKcal(),
                domain.getTargetProteins(),
                domain.getTargetFats(),
                domain.getTargetCarbs(),
                domain.getTargetHydration(),
                domain.getUseAutopilot()
        );
    }
}
