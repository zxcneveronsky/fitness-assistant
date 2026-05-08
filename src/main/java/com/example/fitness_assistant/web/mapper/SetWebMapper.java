package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.web.dto.request.create.CreateSetRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateSetRequest;
import com.example.fitness_assistant.web.dto.response.SetResponse;
import org.springframework.stereotype.Component;

@Component
public class SetWebMapper {
    public Set toDomain(CreateSetRequest request){
        return new Set(
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.sessionId(),
                request.exerciseId(),
                request.weight(),
                request.reps(),
                request.createdAt()

        );
    }
    public Set toDomain(UpdateSetRequest request){
        return new Set(
                request.id(),
                request.sessionId(),
                request.exerciseId(),
                request.weight(),
                request.reps(),
                null // Этого поля нет в запросе, поэтому проставляем null
        );
    }
    public SetResponse toResponse(Set set) {
        return new SetResponse(
                set.getId(),
                set.getSessionId(),
                set.getExerciseId(),
                set.getWeight(),
                set.getReps(),
                set.getCreatedAt()
        );
    }

}
