package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateWorkoutUseCase {

    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Workout updateWorkout(Long userId, Workout workoutUpdate) {
        Long workoutId = workoutUpdate.getId();
        Workout updatedWorkout = workoutRepository.findById(workoutId, userId)
                .map(existingWorkout -> {
                    existingWorkout.setName(workoutUpdate.getName() != null ? workoutUpdate.getName() : existingWorkout.getName());
                    if (workoutUpdate.getExerciseIds() != null) {
                        List<Long> ids = workoutUpdate.getExerciseIds().stream().distinct().toList();
                        if (!exerciseRepository.existsAllByIdIn(ids)) {
                            throw new ExerciseNotFoundException(ids.getFirst());
                        }
                        existingWorkout.setExerciseIds(ids);
                    }
                    return workoutRepository.save(existingWorkout);
                })
                .orElseThrow(() -> new WorkoutNotFoundException(workoutId));
        log.info("Тренировка обновлена | id={}", workoutId);
        return updatedWorkout;
    }
}
