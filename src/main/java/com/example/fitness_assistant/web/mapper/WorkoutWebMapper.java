package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.model.workout.WorkoutWithExercise;
import com.example.fitness_assistant.web.dto.request.create.CreateWorkoutRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateWorkoutRequest;
import com.example.fitness_assistant.web.dto.response.workout.WorkoutResponse;
import com.example.fitness_assistant.web.dto.response.workout.WorkoutWithExerciseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkoutWebMapper {

    private final ExerciseWebMapper exerciseWebMapper;

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
    public WorkoutWithExerciseResponse toResponse(WorkoutWithExercise workout){
        return new WorkoutWithExerciseResponse(
                workout.getId(),
                workout.getName(),
                workout.getExercises().stream().map(exerciseWebMapper::toResponse).toList()
        );
    }
}
