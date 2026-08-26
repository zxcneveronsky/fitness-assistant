package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.WorkoutAccess;
import com.example.fitness_assistant.core.model.WorkoutAccess.AccessLevel;
import com.example.fitness_assistant.core.repository.WorkoutAccessRepository;
import com.example.fitness_assistant.infrastructure.mapper.WorkoutAccessMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.WorkoutAccessEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaWorkoutAccessRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaWorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkoutAccessRepositoryAdapter implements WorkoutAccessRepository {

    private final JpaWorkoutAccessRepository jpaWorkoutAccessRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaWorkoutRepository jpaWorkoutRepository;
    private final WorkoutAccessMapper workoutAccessMapper;

    @Override
    public WorkoutAccess save(WorkoutAccess workoutAccess) {
        WorkoutAccessEntity entity = workoutAccessMapper.toEntity(workoutAccess);
        entity.setOwner(jpaUserRepository.getReferenceById(workoutAccess.getOwnerId()));
        entity.setSharedWithUser(jpaUserRepository.getReferenceById(workoutAccess.getSharedWithUserId()));
        entity.setWorkout(jpaWorkoutRepository.getReferenceById(workoutAccess.getWorkoutId()));
        return workoutAccessMapper.toDomain(jpaWorkoutAccessRepository.save(entity));
    }

    @Override
    public List<WorkoutAccess> findByWorkoutIdAndOwnerId(Long workoutId, Long ownerId) {
        return jpaWorkoutAccessRepository.findByWorkoutIdAndOwnerId(workoutId, ownerId)
                .stream()
                .map(workoutAccessMapper::toDomain)
                .toList();
    }

    @Override
    public List<WorkoutAccess> findAllSharedWithUserId(Long userId) {
        return jpaWorkoutAccessRepository.findAllBySharedWithUserId(userId)
                .stream()
                .map(workoutAccessMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<WorkoutAccess> findByIdAndOwnerId(Long id, Long ownerId) {
        return jpaWorkoutAccessRepository.findByIdAndOwnerId(id, ownerId)
                .map(workoutAccessMapper::toDomain);
    }

    @Override
    public long deleteByIdAndOwnerId(Long id, Long ownerId) {
        return jpaWorkoutAccessRepository.deleteByIdAndOwnerId(id, ownerId);
    }

    @Override
    public boolean existsByWorkoutIdAndOwnerIdAndSharedWithUserId(Long workoutId, Long ownerId, Long sharedWithUserId) {
        return jpaWorkoutAccessRepository.existsByWorkoutIdAndOwnerIdAndSharedWithUserId(workoutId, ownerId, sharedWithUserId);
    }

    @Override
    public boolean existsByWorkoutIdAndSharedWithUserIdAndAccessLevel(Long workoutId, Long userId, AccessLevel accessLevel) {
        return jpaWorkoutAccessRepository.existsByWorkoutIdAndSharedWithUserIdAndAccessLevel(
                workoutId, userId, WorkoutAccessEntity.AccessLevel.valueOf(accessLevel.name()));
    }
}
