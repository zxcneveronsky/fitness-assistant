package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.hydration.Hydration;
import com.example.fitness_assistant.web.dto.request.create.CreateHydrationRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateHydrationRequest;
import com.example.fitness_assistant.web.dto.response.hydration.HydrationResponse;
import org.springframework.stereotype.Component;

@Component
public class HydrationWebMapper {

    public Hydration toDomain(CreateHydrationRequest request) {
        return new Hydration(
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.name(),
                request.amount(),
                request.consumedAt()
        );
    }

    public Hydration toDomain(UpdateHydrationRequest request) {
        return new Hydration(
                request.id(),
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.name(),
                request.amount(),
                request.consumedAt()
        );
    }

    public HydrationResponse toResponse(Hydration hydration) {
        return new HydrationResponse(
                hydration.getId(),
                hydration.getName(),
                hydration.getAmount(),
                hydration.getConsumedAt()
        );
    }
}
