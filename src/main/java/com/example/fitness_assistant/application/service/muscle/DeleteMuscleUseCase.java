package com.example.fitness_assistant.application.service.muscle;

import com.example.fitness_assistant.core.exception.MuscleNotFoundException;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteMuscleUseCase {

    private final MuscleRepository muscleRepository;

    @Transactional
    public void deleteMuscle(Long muscleId) {
        if (!muscleRepository.existsById(muscleId)) {
            throw new MuscleNotFoundException(muscleId);
        }
        muscleRepository.deleteById(muscleId);
        log.info("Мышца удалена | id={}", muscleId);
    }
}
