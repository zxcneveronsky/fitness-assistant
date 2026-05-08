package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ldap.PagedResultsControl;

@Service
@RequiredArgsConstructor
public class UpdateWorkoutUseCase {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;

    @Transactional
    public Workout updateWorkout(UserDetails userDetails, Workout workoutUpdate) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Long workoutId = workoutUpdate.getId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!workoutRepository.existsById(workoutId, userId)) {
            throw new WorkoutNotFoundException(workoutId);
        }
        return workoutRepository.findById(workoutId, userId)
                .map(existingWorkout -> {
                    existingWorkout.setName(workoutUpdate.getName() != null ? workoutUpdate.getName() : existingWorkout.getName());
                    existingWorkout.setExercisesIds(workoutUpdate.getExercisesIds() != null ? workoutUpdate.getExercisesIds() : existingWorkout.getExercisesIds());
                    return workoutRepository.save(existingWorkout);
                })
                .orElseThrow(() -> new WorkoutNotFoundException(workoutId));
    }
}
