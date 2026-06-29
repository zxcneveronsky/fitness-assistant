package com.example.fitness_assistant.application.service.workoutaccess;

import com.example.fitness_assistant.core.model.WorkoutAccess;
import com.example.fitness_assistant.core.repository.WorkoutAccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FindWorkoutAccessUseCase {

    private final WorkoutAccessRepository workoutAccessRepository;

    public List<WorkoutAccess> findByOwnerIdAndWorkoutId(Long userId, Long workoutId) {
        List<WorkoutAccess> accesses = workoutAccessRepository.findByWorkoutIdAndOwnerId(workoutId, userId);
        log.info("Поиск доступов к тренировке завершён | userId={} | workoutId={} | найдено={}", userId, workoutId, accesses.size());
        return accesses;
    }

    public List<WorkoutAccess> findAllSharedWithUserId(Long userId) {
        List<WorkoutAccess> accesses = workoutAccessRepository.findAllSharedWithUserId(userId);
        log.info("Поиск доступов для пользователя завершён | userId={} | найдено={}", userId, accesses.size());
        return accesses;
    }
}
