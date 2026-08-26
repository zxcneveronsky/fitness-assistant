package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Muscle;
import com.example.fitness_assistant.core.repository.MuscleRepository;
import com.example.fitness_assistant.infrastructure.mapper.MuscleMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.MuscleEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaMuscleRepository;
import com.example.fitness_assistant.infrastructure.util.LikePattern;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    @Cacheable("muscle")
    public Optional<Muscle> findById(Long id) {
        return jpaMuscleRepository.findById(id)
                .map(muscleMapper::toDomain);
    }

    @Override
    public List<Muscle> findAllById(List<Long> ids) {
        return jpaMuscleRepository.findAllById(ids).stream()
                .map(muscleMapper::toDomain)
                .toList();
    }

    @Override
    public List<Muscle> searchMuscle(String name) {
        List<MuscleEntity> muscles = (name == null || name.isBlank())
                ? jpaMuscleRepository.findAllByOrderByNameAsc()
                : jpaMuscleRepository.searchByName(LikePattern.escape(name.trim()));
        return muscles.stream()
                .map(muscleMapper::toDomain)
                .toList();
    }

    @Override
    @CacheEvict(value = {"muscle", "exercise"}, allEntries = true)
    public Muscle save(Muscle muscle) {
        return muscleMapper.toDomain(jpaMuscleRepository.save(muscleMapper.toEntity(muscle)));
    }

    @Override
    @CacheEvict(value = {"muscle", "exercise"}, allEntries = true)
    public void deleteById(Long id) {
        jpaMuscleRepository.deleteById(id);
    }

}
