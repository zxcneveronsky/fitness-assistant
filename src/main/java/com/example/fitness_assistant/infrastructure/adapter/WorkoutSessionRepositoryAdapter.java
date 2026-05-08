package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import com.example.fitness_assistant.infrastructure.mapper.WorkoutSessionMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutSessionEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaWorkoutRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaWorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkoutSessionRepositoryAdapter implements WorkoutSessionRepository {

    private final JpaWorkoutSessionRepository jpaWorkoutSessionRepository;
    private final JpaWorkoutRepository jpaWorkoutRepository;
    private final JpaUserRepository jpaUserRepository;
    private final WorkoutSessionMapper workoutSessionMapper;

    @Override
    public Optional<WorkoutSession> findById(Long id, Long userId) {
        return jpaWorkoutSessionRepository.findByIdAndUserId(id, userId)
                .map(workoutSessionMapper::toDomain);
    }

    @Override
    public Page<WorkoutSession> findAllByUserId(Long userId, Pageable pageable) {
        return jpaWorkoutSessionRepository.findAllByUserId(userId, pageable)
                .map(workoutSessionMapper::toDomain);
    }

    @Override
    public WorkoutSession save(WorkoutSession session) {
        WorkoutSessionEntity entity = workoutSessionMapper.toEntity(session);
        entity.setWorkout(jpaWorkoutRepository.getReferenceById(session.getWorkoutId()));
        entity.setUser(jpaUserRepository.getReferenceById(session.getUserId()));
        return workoutSessionMapper.toDomain(jpaWorkoutSessionRepository.save(entity));
    }

    @Override
    public void deleteById(Long id, Long userId) {
        jpaWorkoutSessionRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public boolean existsById(Long id, Long userId) {
        return jpaWorkoutSessionRepository.existsByIdAndUserId(id, userId);
    }
}