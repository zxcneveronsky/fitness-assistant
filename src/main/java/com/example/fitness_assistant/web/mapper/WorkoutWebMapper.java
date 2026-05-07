package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Workout;
import com.example.fitness_assistant.web.dto.request.create.CreateWorkoutRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateWorkoutRequest;
import com.example.fitness_assistant.web.dto.response.WorkoutResponse;
import org.springframework.stereotype.Component;

@Component
public class WorkoutWebMapper {
    public Workout toDomain(CreateWorkoutRequest request){
        return new Workout(
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.name(),
                request.exercisesIds());
    }
    public Workout toDomain(UpdateWorkoutRequest request){
        return new Workout(
                request.id(),
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.name(),
                request.exerciseIds());
    }
    public WorkoutResponse toResponse(Workout workout){
        return new WorkoutResponse(
                workout.getId(),
                workout.getName(),
                workout.getExercisesIds()
        );
    }
}
