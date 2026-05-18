package com.example.fitness_assistant.infrastructure.mapper;

import com.example.fitness_assistant.core.model.hydration.DailyHydration;
import com.example.fitness_assistant.infrastructure.persistence.projection.DailyHydrationProjection;
import org.springframework.stereotype.Component;

@Component
public class DailyHydrationMapper {
    public DailyHydration toDomain(DailyHydrationProjection projection){
        if (projection == null) {
            return new DailyHydration(0.0);
        }
        return new DailyHydration(
                projection.getTotalAmount()
        );
    }

}
