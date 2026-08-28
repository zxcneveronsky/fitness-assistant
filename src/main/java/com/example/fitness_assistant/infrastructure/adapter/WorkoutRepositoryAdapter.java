package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.WorkoutRepository;

import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutAccessEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaWorkoutRepository;
import com.example.fitness_assistant.infrastructure.mapper.WorkoutMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkoutRepositoryAdapter implements WorkoutRepository {

    private final JpaWorkoutRepository jpaWorkoutRepository;
    private final JpaUserRepository jpaUserRepository;
    private final WorkoutMapper workoutMapper;

    @Override
    public Optional<Workout> findById(Long id, Long userId) {
        return jpaWorkoutRepository.findByIdAndUserId(id, userId)
                .map(workoutMapper::toDomain);
    }

    @Override
    public Optional<Workout> findAccessibleById(Long id, Long userId) {
        return jpaWorkoutRepository.findAccessibleById(id, userId)
                .map(workoutMapper::toDomain);
    }

    @Override
    public Optional<Workout> findAccessibleByIdWithLevel(Long id, Long userId, AccessLevel accessLevel) {
        return jpaWorkoutRepository.findAccessibleByIdWithLevel(id, userId, WorkoutAccessEntity.AccessLevel.valueOf(accessLevel.name()))
                .map(workoutMapper::toDomain);
    }

    @Override
    public Page<Workout> findAllByUserId(Long userId, Pageable pageable) {
        return jpaWorkoutRepository.findAllByUserId(userId, pageable)
                .map(workoutMapper::toDomain);
    }

    @Override
    public Page<Workout> searchWorkout(String name, Long userId, Pageable pageable) {
        Page<WorkoutEntity> page = (name == null || name.isBlank())
                ? jpaWorkoutRepository.findAllByUserId(userId, pageable)
                : jpaWorkoutRepository.searchByName(name.trim(), userId, pageable);
        return page.map(workoutMapper::toDomain);
    }

    @Override
    public List<Workout> findAllById(List<Long> ids) {
        return jpaWorkoutRepository.findAllById(ids).stream()
                .map(workoutMapper::toDomain)
                .toList();
    }

    @Override
    public boolean hasAccess(Long workoutId, Long userId) {
        return jpaWorkoutRepository.existsAccessible(workoutId, userId);
    }

    @Override
    public List<Workout> findAllAccessibleByIdIn(List<Long> ids, Long userId) {
        return jpaWorkoutRepository.findAllAccessibleByIdIn(ids, userId).stream()
                .map(workoutMapper::toDomain)
                .toList();
    }

    @Override
    public Workout save(Workout workout) {
        Long userId = workout.getUserId();
        WorkoutEntity workoutEntity = workoutMapper.toEntity(workout);
        workoutEntity.setUser(jpaUserRepository.getReferenceById(userId));
        return workoutMapper.toDomain(jpaWorkoutRepository.save(workoutEntity));
    }

    @Override
    public long deleteById(Long id, Long userId) {
        return jpaWorkoutRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public boolean existsById(Long id, Long userId) {
        return jpaWorkoutRepository.existsByIdAndUserId(id, userId);
    }
}