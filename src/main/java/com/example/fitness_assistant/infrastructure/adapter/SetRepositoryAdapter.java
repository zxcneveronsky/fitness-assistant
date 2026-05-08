package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.infrastructure.mapper.SetMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.SetEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaExerciseRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaSetRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaWorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SetRepositoryAdapter implements SetRepository {

    private final JpaSetRepository jpaSetRepository;
    private final JpaWorkoutSessionRepository jpaWorkoutSessionRepository;
    private final JpaExerciseRepository jpaExerciseRepository;
    private final SetMapper setMapper;

    @Override
    public Set save(Set set) {
        SetEntity setEntity = setMapper.toEntity(set);
        setEntity.setSession(jpaWorkoutSessionRepository.getReferenceById(set.getSessionId()));
        setEntity.setExercise(jpaExerciseRepository.getReferenceById(set.getExerciseId()));
        return setMapper.toDomain(jpaSetRepository.save(setEntity));
    }

    @Override
    public Page<Set> findAllBySessionIdAndExerciseId(Long sessionId, Long exerciseId, Pageable pageable) {
        return jpaSetRepository.findAllBySessionIdAndExerciseId(sessionId, exerciseId, pageable)
                .map(setMapper::toDomain);
    }

    @Override
    public Optional<Set> findById(Long id, Long sessionId) {
        return jpaSetRepository.findByIdAndSessionId(id, sessionId)
                .map(setMapper::toDomain);
    }

    @Override
    public void deleteById(Long id, Long sessionId) {
        jpaSetRepository.deleteByIdAndSessionId(id, sessionId);
    }

    @Override
    public boolean existsById(Long id, Long sessionId) {
        return jpaSetRepository.existsByIdAndSessionId(id, sessionId);
    }
}