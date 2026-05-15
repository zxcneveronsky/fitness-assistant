package com.example.fitness_assistant.application.service.set;

import com.example.fitness_assistant.core.exception.SetNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindSetUseCase {
    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;

    public Set findById(Long id, Long sessionId, UserDetails userDetails) {
        Long userId = ((UserDetailsAdapter) userDetails).getUserId();
        if (!workoutSessionRepository.existsById(sessionId, userId)) {
            throw new WorkoutSessionNotFoundException(sessionId);
        }
        Set set = setRepository.findById(id, sessionId)
                .orElseThrow(() -> new SetNotFoundException(id));
        log.info("Подход найден | id={}", id);
        return set;
    }


}
