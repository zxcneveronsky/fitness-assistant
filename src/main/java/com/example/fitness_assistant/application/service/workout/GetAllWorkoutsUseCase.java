package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllWorkoutsUseCase {
    private final WorkoutRepository workoutRepository;

    @Transactional(readOnly = true)
    public Page<Workout> getAllWorkouts(Long userId, Pageable pageable) {
        Page<Workout> workouts = workoutRepository.findAllByUserId(userId, pageable);
        log.info("Поиск тренировок завершён | найдено={} | страница={}/{}",
                workouts.getTotalElements(), workouts.getNumber() + 1, workouts.getTotalPages());
        return workouts;
    }

}
