package com.example.fitness_assistant.infrastructure.adapter;

import com.example.fitness_assistant.core.model.BodyWeight;
import com.example.fitness_assistant.core.repository.BodyWeightRepository;
import com.example.fitness_assistant.infrastructure.mapper.BodyWeightMapper;
import com.example.fitness_assistant.infrastructure.persistence.entity.BodyWeightEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaBodyWeightRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BodyWeightRepositoryAdapter implements BodyWeightRepository {

    private final JpaBodyWeightRepository jpaBodyWeightRepository;
    private final JpaUserRepository jpaUserRepository;
    private final BodyWeightMapper bodyWeightMapper;

    @Override
    public Optional<BodyWeight> findById(Long id, Long userId) {
        return jpaBodyWeightRepository.findByIdAndUserId(id, userId)
                .map(bodyWeightMapper::toDomain);
    }

    @Override
    public List<BodyWeight> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to) {
        return jpaBodyWeightRepository.findByUserIdAndMeasuredAtBetweenOrderByMeasuredAtDescIdDesc(userId, from, to)
                .stream()
                .map(bodyWeightMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<BodyWeight> findLatestByUserId(Long userId) {
        return jpaBodyWeightRepository.findTopByUserIdOrderByMeasuredAtDescIdDesc(userId)
                .map(bodyWeightMapper::toDomain);
    }

    @Override
    public BodyWeight save(BodyWeight bodyWeight) {
        BodyWeightEntity bodyWeightEntity = bodyWeightMapper.toEntity(bodyWeight);
        bodyWeightEntity.setUser(jpaUserRepository.getReferenceById(bodyWeight.getUserId()));
        return bodyWeightMapper.toDomain(jpaBodyWeightRepository.save(bodyWeightEntity));
    }

    @Override
    public long deleteById(Long id, Long userId) {
        return jpaBodyWeightRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public boolean existsById(Long id, Long userId) {
        return jpaBodyWeightRepository.existsByIdAndUserId(id, userId);
    }
}
