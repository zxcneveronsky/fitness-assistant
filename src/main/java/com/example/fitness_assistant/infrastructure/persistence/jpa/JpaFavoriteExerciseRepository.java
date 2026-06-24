package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.ExerciseEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.FavoriteExerciseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaFavoriteExerciseRepository extends JpaRepository<FavoriteExerciseEntity, Long> {

    boolean existsByUserIdAndExerciseId(Long userId, Long exerciseId);

    void deleteByUserIdAndExerciseId(Long userId, Long exerciseId);

    @Query(value = """
        SELECT DISTINCT e FROM ExerciseEntity e
        LEFT JOIN FETCH e.muscles m
        WHERE e.id IN (SELECT fe.exercise.id FROM FavoriteExerciseEntity fe WHERE fe.user.id = :userId)
        AND (cast(:name as text) IS NULL
        OR LOWER(e.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%'))
        OR LOWER(m.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%')))
        AND (:muscleId IS NULL OR m.id = :muscleId)
        ORDER BY e.name ASC
        """,
            countQuery = """
        SELECT COUNT(DISTINCT e) FROM ExerciseEntity e
        LEFT JOIN e.muscles m
        WHERE e.id IN (SELECT fe.exercise.id FROM FavoriteExerciseEntity fe WHERE fe.user.id = :userId)
        AND (cast(:name as text) IS NULL
        OR LOWER(e.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%'))
        OR LOWER(m.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%')))
        AND (:muscleId IS NULL OR m.id = :muscleId)
        """)
    Page<ExerciseEntity> searchFavoriteExercises(@Param("userId") Long userId, @Param("name") String name, @Param("muscleId") Long muscleId, Pageable pageable);

    @Query("SELECT fe.exercise.id FROM FavoriteExerciseEntity fe WHERE fe.user.id = :userId ORDER BY fe.exercise.id ASC")
    List<Long> findIdsByUserId(@Param("userId") Long userId);
}
