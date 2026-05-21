package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import com.example.fitness_assistant.infrastructure.mapper.MuscleMapper;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaMuscleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MuscleRepositoryAdapter implements MuscleRepository {
    private final JpaMuscleRepository jpaMuscleRepository;
    private final MuscleMapper muscleMapper;

    @Override
    public boolean existsById(Long id){
        return jpaMuscleRepository.existsById(id);
    }

    @Override
    public List<Muscle> findAllById(List<Long> ids) {
        return jpaMuscleRepository.findAllById(ids).stream()
                .map(muscleMapper::toDomain)
                .toList();
    }

    @Override
    public List<Muscle> searchMuscles(String name) {
        return jpaMuscleRepository.searchMuscles(name).stream()
                .map(muscleMapper::toDomain)
                .toList();
    }

}
