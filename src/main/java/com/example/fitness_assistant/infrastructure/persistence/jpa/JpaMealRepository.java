package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.MealEntity;
import com.example.fitness_assistant.infrastructure.persistence.projection.DailyNutritionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;

public interface JpaMealRepository extends JpaRepository<MealEntity, Long> {
    @EntityGraph(attributePaths = "user")
    Optional<MealEntity> findByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
    void deleteByIdAndUserId(Long id, Long userId);

    @Query("SELECT h FROM MealEntity h WHERE h.user.id = :userId " +
            "AND (CAST(:startOfDay AS timestamp) IS NULL " +
            "OR (h.consumedAt >= :startOfDay AND h.consumedAt < :endOfDay))")
    Page<MealEntity> searchMeal(@Param("userId") Long userId,
                            @Param("startOfDay") LocalDateTime startOfDay,
                            @Param("endOfDay") LocalDateTime endOfDay,
                            Pageable pageable);

    @Query("SELECT COALESCE(SUM(m.kcal), 0) AS kcal, " +
            "COALESCE(SUM(m.proteins), 0) AS proteins, " +
            "COALESCE(SUM(m.fats), 0) AS fats, " +
            "COALESCE(SUM(m.carbs), 0) AS carbs " +
            "FROM MealEntity m " +
            "WHERE m.user.id = :userId " +
            "AND m.consumedAt >= :startOfDay " +
            "AND m.consumedAt < :endOfDay")
    DailyNutritionProjection getDailyNutrition(@Param("userId") Long userId,
                                               @Param("startOfDay") LocalDateTime startOfDay,
                                               @Param("endOfDay") LocalDateTime endOfDay);
}
