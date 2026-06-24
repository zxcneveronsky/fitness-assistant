package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.sessiondetail.SessionDetail;
import com.example.fitness_assistant.web.dto.response.workoutsession.ExerciseSetsResponse;
import com.example.fitness_assistant.web.dto.response.workoutsession.SessionDetailResponse;
import com.example.fitness_assistant.web.dto.response.workoutsession.SetItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionDetailWebMapper {

    public SessionDetailResponse toResponse(SessionDetail detail) {
        return new SessionDetailResponse(
                detail.getId(),
                detail.getStartTime(),
                detail.getEndTime(),
                detail.getExercises().stream()
                        .map(ex -> new ExerciseSetsResponse(
                                ex.getExerciseId(),
                                ex.getName(),
                                ex.getSets().stream()
                                        .map(s -> new SetItemResponse(
                                                s.getId(),
                                                s.getWeight(),
                                                s.getReps(),
                                                s.getCreatedAt()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
    }
}
