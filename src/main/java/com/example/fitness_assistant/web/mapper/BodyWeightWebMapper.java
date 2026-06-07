package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.BodyWeight;
import com.example.fitness_assistant.web.dto.request.create.CreateBodyWeightRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateBodyWeightRequest;
import com.example.fitness_assistant.web.dto.response.BodyWeightResponse;
import org.springframework.stereotype.Component;

@Component
public class BodyWeightWebMapper {

    public BodyWeight toDomain(CreateBodyWeightRequest request) {
        return new BodyWeight(
                null,
                null,
                request.weightKg(),
                request.measuredAt()
        );
    }

    public BodyWeight toDomain(UpdateBodyWeightRequest request) {
        return new BodyWeight(
                request.id(),
                null,
                request.weightKg(),
                request.measuredAt()
        );
    }

    public BodyWeightResponse toResponse(BodyWeight bodyWeight) {
        return new BodyWeightResponse(
                bodyWeight.getId(),
                bodyWeight.getWeightKg(),
                bodyWeight.getMeasuredAt()
        );
    }
}
