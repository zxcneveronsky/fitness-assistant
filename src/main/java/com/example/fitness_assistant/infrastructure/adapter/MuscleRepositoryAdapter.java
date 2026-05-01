package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import com.example.fitness_assistant.infrastructure.mapper.MuscleMapper;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaMuscleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MuscleRepositoryAdapter implements MuscleRepository {
    private final JpaMuscleRepository jpaMuscleRepository;
    private final MuscleMapper muscleMapper;

    @Override
    public Muscle getReferenceById(Long id){
        return muscleMapper.toDomain(jpaMuscleRepository.getReferenceById(id));
    }

}
