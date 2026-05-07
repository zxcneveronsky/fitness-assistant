package com.example.fitness_assistant.application.service.workout;

import com.example.fitness_assistant.core.model.Workout;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAllWorkoutsUseCase {
    private final WorkoutRepository workoutRepository;

    @Transactional
    public Page<Workout> getAllWorkouts(UserDetails userDetails, Pageable pageable) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        return workoutRepository.findAllByUserId(userId, pageable);
    }

}
