package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.exercise.Exercise;
import com.example.fitness_assistant.core.model.FavoriteExercise;
import com.example.fitness_assistant.core.repository.FavoriteExerciseRepository;
import com.example.fitness_assistant.infrastructure.mapper.ExerciseMapper;
import com.example.fitness_assistant.infrastructure.mapper.FavoriteExerciseMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.FavoriteExerciseEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaExerciseRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaFavoriteExerciseRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FavoriteExerciseRepositoryAdapter implements FavoriteExerciseRepository {

    private final JpaFavoriteExerciseRepository jpaFavoriteExerciseRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaExerciseRepository jpaExerciseRepository;
    private final FavoriteExerciseMapper favoriteExerciseMapper;
    private final ExerciseMapper exerciseMapper;

    @Override
    public boolean existsByExerciseIdAndUserId(Long exerciseId, Long userId) {
        return jpaFavoriteExerciseRepository.existsByExerciseIdAndUserId(exerciseId, userId);
    }

    @Override
    public FavoriteExercise save(FavoriteExercise favoriteExercise) {
        FavoriteExerciseEntity favoriteExerciseEntity = favoriteExerciseMapper.toEntity(favoriteExercise);
        favoriteExerciseEntity.setUser(jpaUserRepository.getReferenceById(favoriteExercise.getUserId()));
        favoriteExerciseEntity.setExercise(jpaExerciseRepository.getReferenceById(favoriteExercise.getExerciseId()));
        return favoriteExerciseMapper.toDomain(jpaFavoriteExerciseRepository.save(favoriteExerciseEntity));
    }

    @Override
    public void deleteByExerciseIdAndUserId(Long exerciseId, Long userId) {
        jpaFavoriteExerciseRepository.deleteByExerciseIdAndUserId(exerciseId, userId);
    }

    @Override
    public Page<Exercise> searchFavoriteExercise(String name, Long muscleId, Long userId, Pageable pageable) {
        return jpaFavoriteExerciseRepository.searchFavoriteExercises(name, muscleId, userId, pageable)
                .map(exerciseMapper::toDomain);
    }

    @Override
    public List<Long> findIdsByUserId(Long userId) {
        return jpaFavoriteExerciseRepository.findIdsByUserId(userId);
    }
}
