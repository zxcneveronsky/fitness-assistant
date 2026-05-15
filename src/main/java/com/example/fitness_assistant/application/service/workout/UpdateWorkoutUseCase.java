package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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
        Workout updatedWorkout = workoutRepository.findById(workoutId, userId)
                .map(existingWorkout -> {
                    existingWorkout.setName(workoutUpdate.getName() != null ? workoutUpdate.getName() : existingWorkout.getName());
                    existingWorkout.setExercisesIds(workoutUpdate.getExercisesIds() != null ? workoutUpdate.getExercisesIds() : existingWorkout.getExercisesIds());
                    return workoutRepository.save(existingWorkout);
                })
                .orElseThrow(() -> new WorkoutNotFoundException(workoutId));
        log.info("Тренировка обновлена | id={}", updatedWorkout.getId());
        return updatedWorkout;
    }
}
