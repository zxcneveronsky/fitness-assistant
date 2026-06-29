package com.example.fitness_assistant.application.service.workoutaccess;

import com.example.fitness_assistant.core.exception.WorkoutAccessNotFoundException;
import com.example.fitness_assistant.core.model.WorkoutAccess;
import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;
import com.example.fitness_assistant.core.repository.WorkoutAccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateWorkoutAccessUseCase {

    private final WorkoutAccessRepository workoutAccessRepository;

    @Transactional
    public WorkoutAccess updateWorkoutAccess(Long userId, WorkoutAccess workoutAccessUpdate) {
        Long accessId = workoutAccessUpdate.getId();
        WorkoutAccess updatedWorkoutAccess = workoutAccessRepository.findByIdAndOwnerId(accessId, userId)
                .map(existingWorkoutAccess -> {
                    if (workoutAccessUpdate.getAccessLevel() != null) {
                        existingWorkoutAccess.setAccessLevel(workoutAccessUpdate.getAccessLevel());
                    }
                    return workoutAccessRepository.save(existingWorkoutAccess);
                })
                .orElseThrow(() -> new WorkoutAccessNotFoundException(accessId));
        log.info("Доступ к тренировке обновлён | id={} | accessLevel={}", accessId, updatedWorkoutAccess.getAccessLevel());
        return updatedWorkoutAccess;
    }
}
