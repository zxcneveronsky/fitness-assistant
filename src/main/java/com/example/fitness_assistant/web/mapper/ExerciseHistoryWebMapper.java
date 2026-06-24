package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.ExerciseHistory;
import com.example.fitness_assistant.web.dto.response.ExerciseHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExerciseHistoryWebMapper {

    private final SetWebMapper setWebMapper;

    public ExerciseHistoryResponse toResponse(ExerciseHistory point) {
        return new ExerciseHistoryResponse(
                point.getSessionId(),
                point.getName(),
                point.getStartTime(),
                point.getEndTime(),
                point.getSets().stream()
                        .map(setWebMapper::toResponse)
                        .toList()
        );
    }
}
