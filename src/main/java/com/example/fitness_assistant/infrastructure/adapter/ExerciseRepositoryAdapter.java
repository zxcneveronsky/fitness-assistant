package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.exercise.Exercise;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.infrastructure.persistence.entity.ExerciseEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaExerciseRepository;
import com.example.fitness_assistant.infrastructure.mapper.ExerciseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExerciseRepositoryAdapter implements ExerciseRepository {

    private final JpaExerciseRepository jpaExerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Override
    @Cacheable("exercise")
    public Optional<Exercise> findById(Long id) {
        return jpaExerciseRepository.findById(id)
                .map(exerciseMapper::toDomain);
    }
    @Override
    public List<Exercise> findAllByIdIn(List<Long> ids) {
        return jpaExerciseRepository.findAllByIdIn(ids).stream().map(exerciseMapper::toDomain).toList();
    }

    @Override
    public Page<Exercise> searchExercise(String name, Long muscleId, Pageable pageable) {
        Page<Long> idPage = findIdPage(name, muscleId, pageable);
        return fetchExercisesPage(idPage, pageable);
    }

    private Page<Long> findIdPage(String name, Long muscleId, Pageable pageable) {
        boolean hasName = name != null && !name.isBlank();
        String trimmed = hasName ? name.trim() : null;
        if (!hasName && muscleId == null) {
            return jpaExerciseRepository.findPageIds(pageable);
        }
        if (!hasName) {
            return jpaExerciseRepository.findPageIdsByMuscleId(muscleId, pageable);
        }
        if (muscleId == null) {
            return jpaExerciseRepository.findPageIdsByName(trimmed, pageable);
        }
        return jpaExerciseRepository.findPageIdsByMuscleIdAndName(muscleId, trimmed, pageable);
    }

    private Page<Exercise> fetchExercisesPage(Page<Long> idPage, Pageable pageable) {
        if (idPage.isEmpty()) {
            return Page.empty(pageable);
        }
        List<ExerciseEntity> entities = jpaExerciseRepository.findAllByIdIn(idPage.getContent());
        Map<Long, ExerciseEntity> byId = entities.stream()
                .collect(Collectors.toMap(ExerciseEntity::getId, e -> e));
        List<Exercise> content = idPage.getContent().stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .map(exerciseMapper::toDomain)
                .toList();
        return new PageImpl<>(content, pageable, idPage.getTotalElements());
    }

    @Override
    public boolean existsAllByIdIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return true;
        return jpaExerciseRepository.countAllByIdIn(ids) == ids.size();
    }

    @Override
    @CacheEvict(value = "exercise", allEntries = true)
    public Exercise save(Exercise exercise) {
        return exerciseMapper.toDomain(jpaExerciseRepository.save(exerciseMapper.toEntity(exercise)));
    }

    @Override
    @CacheEvict(value = "exercise", allEntries = true)
    public void deleteById(Long id) {
        jpaExerciseRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaExerciseRepository.existsById(id);
    }
}