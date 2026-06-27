package com.example.fitness_assistant.application.service.workoutaccess;

import com.example.fitness_assistant.core.model.workoutaccess.WorkoutAccess;
import com.example.fitness_assistant.core.repository.WorkoutAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindWorkoutAccessUseCase {

    private final WorkoutAccessRepository workoutAccessRepository;

    public List<WorkoutAccess> findByWorkoutIdAndOwnerId(Long userId, Long workoutId) {
        return workoutAccessRepository.findByWorkoutIdAndOwnerId(workoutId, userId);
    }

    public List<WorkoutAccess> findAllSharedWithUserId(Long userId) {
        return workoutAccessRepository.findAllSharedWithUserId(userId);
    }
}
