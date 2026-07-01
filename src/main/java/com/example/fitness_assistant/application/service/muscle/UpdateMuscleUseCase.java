package com.example.fitness_assistant.application.service.muscle;

import com.example.fitness_assistant.core.exception.MuscleNotFoundException;
import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateMuscleUseCase {

    private final MuscleRepository muscleRepository;

    @Transactional
    public Muscle updateMuscle(Muscle muscleUpdate) {
        Long muscleId = muscleUpdate.getId();
        Muscle updatedMuscle = muscleRepository.findById(muscleId)
                .map(existingMuscle -> {
                    existingMuscle.setName(muscleUpdate.getName() != null ? muscleUpdate.getName() : existingMuscle.getName());
                    return muscleRepository.save(existingMuscle);
                })
                .orElseThrow(() -> new MuscleNotFoundException(muscleId));
        log.info("Мышца обновлена | id={}", muscleId);
        return updatedMuscle;
    }
}
