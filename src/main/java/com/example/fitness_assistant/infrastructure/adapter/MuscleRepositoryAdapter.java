package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import com.example.fitness_assistant.infrastructure.mapper.MuscleMapper;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaMuscleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable("muscles")
    public Muscle getReferenceById(Long id){
        return muscleMapper.toDomain(jpaMuscleRepository.getReferenceById(id));
    }

    @Override
    @Cacheable("muscles")
    public List<Muscle> findAll() {
        return jpaMuscleRepository.findAll().stream()
                .map(muscleMapper::toDomain)
                .toList();
    }

}
