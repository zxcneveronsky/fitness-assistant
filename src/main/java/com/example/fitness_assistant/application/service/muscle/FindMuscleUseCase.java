package com.example.fitness_assistant.application.service.muscle;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindMuscleUseCase {

    private final MuscleRepository muscleRepository;

    @Transactional(readOnly = true)
    public List<Muscle> searchMuscles(String name) {
        List<Muscle> muscles = muscleRepository.searchMuscles(name);
        log.info("Поиск мышц | name='{}' | найдено={}", name, muscles.size());
        return muscles;
    }
}
