package com.example.fitness_assistant.application.service.workoutaccess;

import com.example.fitness_assistant.core.exception.WorkoutAccessNotFoundException;
import com.example.fitness_assistant.core.model.workoutaccess.WorkoutAccess;
import com.example.fitness_assistant.core.repository.WorkoutAccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteWorkoutAccessUseCase {

    private final WorkoutAccessRepository workoutAccessRepository;

    @Transactional
    public void deleteWorkoutAccess(Long userId, Long accessId) {
        WorkoutAccess existingWorkoutAccess = workoutAccessRepository.findByIdAndOwnerId(accessId, userId)
                .orElseThrow(() -> new WorkoutAccessNotFoundException(accessId));
        workoutAccessRepository.deleteById(existingWorkoutAccess.getId());
        log.info("Доступ к тренировке удалён | id={}", accessId);
    }
}
