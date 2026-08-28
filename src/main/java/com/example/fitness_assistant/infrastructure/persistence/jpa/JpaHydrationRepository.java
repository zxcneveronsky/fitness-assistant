package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.HydrationEntity;
import com.example.fitness_assistant.infrastructure.persistence.projection.DailyHydrationProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaHydrationRepository extends JpaRepository<HydrationEntity, Long> {
    @EntityGraph(attributePaths = "user")
    Optional<HydrationEntity> findByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query("DELETE FROM HydrationEntity h WHERE h.id = :id AND h.user.id = :userId")
    long deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @EntityGraph(attributePaths = "user")
    @Query("""
            SELECT h FROM HydrationEntity h
            WHERE h.user.id = :userId
            AND h.consumedAt >= :startOfDay AND h.consumedAt < :endOfDay
            ORDER BY h.consumedAt DESC
            """)
    Page<HydrationEntity> searchHydration(@Param("userId") Long userId,
                                          @Param("startOfDay") LocalDateTime startOfDay,
                                          @Param("endOfDay") LocalDateTime endOfDay,
                                          Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(h.amount), 0) AS totalAmount
            FROM HydrationEntity h
            WHERE h.user.id = :userId
            AND h.consumedAt >= :startOfDay
            AND h.consumedAt < :endOfDay
            """)
    DailyHydrationProjection getDailyHydration(@Param("userId") Long userId,
                                           @Param("startOfDay") LocalDateTime startOfDay,
                                           @Param("endOfDay") LocalDateTime endOfDay);
}
