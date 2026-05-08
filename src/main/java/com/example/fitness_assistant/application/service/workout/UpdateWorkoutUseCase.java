package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateWorkoutUseCase {

    private final WorkoutRepository workoutRepository;

    @Transactional
    public Workout updateWorkout(UserDetails userDetails, Workout workoutUpdate) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Long workoutId = workoutUpdate.getId();

        return workoutRepository.findById(workoutId, userId)
                .map(existingWorkout -> {
                    existingWorkout.setName(workoutUpdate.getName() != null ? workoutUpdate.getName() : existingWorkout.getName());
                    existingWorkout.setExercisesIds(workoutUpdate.getExercisesIds() != null ? workoutUpdate.getExercisesIds() : existingWorkout.getExercisesIds());
                    return workoutRepository.save(existingWorkout);
                })
                .orElseThrow(() -> new WorkoutNotFoundException(workoutId));
    }
}
