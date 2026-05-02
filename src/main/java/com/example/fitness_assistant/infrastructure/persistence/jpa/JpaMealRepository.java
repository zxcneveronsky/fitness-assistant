package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.MealEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface JpaMealRepository extends JpaRepository<MealEntity, Long> {
    Optional<MealEntity> findByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
    void deleteByIdAndUserId(Long id, Long userId);


    @Query("SELECT m FROM MealEntity m WHERE m.user.id = :userId " +
            "AND (:date IS NULL OR m.consumedAt = :date)")
    Page<MealEntity> searchMeal(@Param("userId") Long userId,
                            @Param("date") LocalDate date,
                            Pageable pageable);
}
