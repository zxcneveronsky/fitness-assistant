package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateSetUseCase {
    private final UserRepository userRepository;
    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public Set createSet(UserDetails userDetails, Set set) {
        set.setId(null);
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        Long sessionId = set.getSessionId();
        Long exerciseId = set.getExerciseId();
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        if (!exerciseRepository.existsById(exerciseId)) {
            throw new ExerciseNotFoundException(exerciseId);
        }
        return setRepository.save(set);
    }

}
