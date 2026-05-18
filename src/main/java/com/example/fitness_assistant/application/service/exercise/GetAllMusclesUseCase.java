package com.example.fitness_assistant.application.service.exercise;

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
public class GetAllMusclesUseCase {

    private final MuscleRepository muscleRepository;

    @Transactional(readOnly = true)
    public List<Muscle> getAllMuscles() {
        List<Muscle> muscles = muscleRepository.findAll();
        log.info("Получены все мышцы | найдено={}", muscles.size());
        return muscles;
    }
}
