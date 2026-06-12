package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Streak;
import com.example.fitness_assistant.core.repository.StreakRepository;
import com.example.fitness_assistant.infrastructure.mapper.StreakMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.StreakEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.UserEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaStreakRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StreakRepositoryAdapter implements StreakRepository {

    private final JpaStreakRepository jpaStreakRepository;
    private final StreakMapper streakMapper;
    private final EntityManager entityManager;

    @Override
    public Optional<Streak> findById(Long userId) {
        return jpaStreakRepository.findById(userId)
                .map(streakMapper::toDomain);
    }

    @Override
    public Streak save(Streak streak) {
        StreakEntity streakEntity = streakMapper.toEntity(streak);
        streakEntity.setUser(entityManager.getReference(UserEntity.class, streak.getUserId()));
        if (!jpaStreakRepository.existsById(streak.getUserId())) {
            entityManager.persist(streakEntity);
            return streakMapper.toDomain(streakEntity);
        } else {
            StreakEntity mergedStreakEntity = entityManager.merge(streakEntity);
            return streakMapper.toDomain(mergedStreakEntity);
        }
    }
}
