package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaExerciseRepository;
import com.example.fitness_assistant.infrastructure.mapper.ExerciseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExerciseRepositoryAdapter implements ExerciseRepository {

    private final JpaExerciseRepository jpaExerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Override
    @Cacheable("exercises")
    public Optional<Exercise> findById(Long id) {
        return jpaExerciseRepository.findById(id)
                .map(exerciseMapper::toDomain);
    }
    @Override
    @Cacheable("exercises")
    public List<Exercise> findAllByIdIn(List<Long> id) {
        return jpaExerciseRepository.findAllByIdIn(id).stream().map(exerciseMapper::toDomain).toList();
    }

    @Override
    public Page<Exercise> searchExercise(String name, Long muscleId, Pageable pageable) {
        return jpaExerciseRepository.searchExercise(name, muscleId, pageable)
                .map(exerciseMapper::toDomain);
    }

    @Override
    @CacheEvict(value = "exercises", allEntries = true)
    public Exercise save(Exercise exercise) {
        return exerciseMapper.toDomain(jpaExerciseRepository.save(exerciseMapper.toEntity(exercise)));
    }

    @Override
    @CacheEvict(value = "exercises", allEntries = true)
    public void deleteById(Long id) {
        jpaExerciseRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaExerciseRepository.existsById(id);
    }
}