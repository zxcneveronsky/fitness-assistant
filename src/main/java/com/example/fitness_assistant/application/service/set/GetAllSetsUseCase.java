package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class GetAllSetsUseCase {
    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseRepository exerciseRepository;

    public Page<Set> getAllSets(Long sessionId, Long exerciseId, UserDetails userDetails, Pageable pageable) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        if (!exerciseRepository.existsById(exerciseId)){
            throw new ExerciseNotFoundException(exerciseId);
        }
        return setRepository.findAllBySessionIdAndExerciseId(sessionId, exerciseId, pageable);
    }

}
