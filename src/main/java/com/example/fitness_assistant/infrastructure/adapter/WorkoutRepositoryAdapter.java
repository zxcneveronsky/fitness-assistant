package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.WorkoutRepository;

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
    public Optional<Workout> findById(Long id,Long userId) {
        return jpaWorkoutRepository.findByIdAndUserId(id,userId)
                .map(workoutMapper::toDomain);
    }

    @Override
    public Page<Workout> findAllByUserId(Long userId,Pageable pageable) {
        return jpaWorkoutRepository.findAllByUserId(userId,pageable)
                .map(workoutMapper::toDomain);
    }

    @Override
    public Page<Workout> searchWorkout(String name,Long userId, Pageable pageable) {
        return jpaWorkoutRepository.searchWorkout(name, userId, pageable)
                .map(workoutMapper::toDomain);
    }

    @Override
    public List<Workout> findAllById(List<Long> ids) {
        return jpaWorkoutRepository.findAllById(ids).stream()
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
    public void deleteById(Long id,Long userId) {
        jpaWorkoutRepository.deleteByIdAndUserId(id,userId);
    }

    @Override
    public boolean existsById(Long id,Long userId) {
        return jpaWorkoutRepository.existsByIdAndUserId(id,userId);
    }
}