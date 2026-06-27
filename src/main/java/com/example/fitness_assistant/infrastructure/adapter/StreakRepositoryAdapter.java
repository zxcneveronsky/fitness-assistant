package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.Streak;
import com.example.fitness_assistant.core.repository.StreakRepository;
import com.example.fitness_assistant.infrastructure.mapper.StreakMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.StreakEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaStreakRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StreakRepositoryAdapter implements StreakRepository {

    private final EntityManager entityManager;
    private final JpaStreakRepository jpaStreakRepository;
    private final JpaUserRepository jpaUserRepository;
    private final StreakMapper streakMapper;

    @Override
    public Optional<Streak> findById(Long userId) {
        return jpaStreakRepository.findById(userId)
                .map(streakMapper::toDomain);
    }

    @Override
    public Streak save(Streak streak) {
        StreakEntity entity = streakMapper.toEntity(streak);
        entity.setUser(jpaUserRepository.getReferenceById(streak.getUserId()));
        if (entity.getId() != null && jpaStreakRepository.existsById(entity.getId())) {
            StreakEntity merged = entityManager.merge(entity);
            return streakMapper.toDomain(merged);
        }
        entityManager.persist(entity);
        return streakMapper.toDomain(entity);
    }
}
