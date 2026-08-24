package com.example.fitness_assistant.application.service.muscle;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateMuscleUseCase {

    private final MuscleRepository muscleRepository;

    @Transactional
    public Muscle createMuscle(Muscle muscle) {
        Muscle newMuscle = new Muscle(null, muscle.getName());
        Muscle savedMuscle = muscleRepository.save(newMuscle);
        log.info("Мышца создана | id={} | название='{}'",
                savedMuscle.getId(), savedMuscle.getName());
        return savedMuscle;
    }
}
