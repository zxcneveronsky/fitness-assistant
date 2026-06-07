package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.BodyWeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaBodyWeightRepository extends JpaRepository<BodyWeightEntity, Long> {

    List<BodyWeightEntity> findByUserIdAndMeasuredAtBetweenOrderByMeasuredAtDescIdDesc(Long userId, LocalDate from, LocalDate to);

    Optional<BodyWeightEntity> findTopByUserIdOrderByMeasuredAtDescIdDesc(Long userId);

    Optional<BodyWeightEntity> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}
