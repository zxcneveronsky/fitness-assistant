package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.BodyWeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaBodyWeightRepository extends JpaRepository<BodyWeightEntity, Long> {

    List<BodyWeightEntity> findByUserIdAndMeasuredAtBetweenOrderByMeasuredAtDescIdDesc(Long userId, LocalDate from, LocalDate to);

    Optional<BodyWeightEntity> findTopByUserIdOrderByMeasuredAtDescIdDesc(Long userId);

    Optional<BodyWeightEntity> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("DELETE FROM BodyWeightEntity b WHERE b.id = :id AND b.user.id = :userId")
    long deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);
}
