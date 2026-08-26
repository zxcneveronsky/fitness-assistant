package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.FavoriteExerciseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaFavoriteExerciseRepository extends JpaRepository<FavoriteExerciseEntity, Long> {

    boolean existsByExerciseIdAndUserId(Long exerciseId, Long userId);

    @Modifying
    @Query("DELETE FROM FavoriteExerciseEntity fe WHERE fe.exercise.id = :exerciseId AND fe.user.id = :userId")
    long deleteByExerciseIdAndUserId(@Param("exerciseId") Long exerciseId, @Param("userId") Long userId);

    @Query("SELECT fe.exercise.id FROM FavoriteExerciseEntity fe WHERE fe.user.id = :userId ORDER BY fe.exercise.id ASC")
    List<Long> findIdsByUserId(@Param("userId") Long userId);

    @Query(value = """
            SELECT e.id FROM ExerciseEntity e
            WHERE EXISTS (SELECT 1 FROM FavoriteExerciseEntity fe
                          WHERE fe.exercise.id = e.id AND fe.user.id = :userId)
            GROUP BY e.id, e.name
            ORDER BY e.name ASC
            """,
            countQuery = """
            SELECT COUNT(e.id) FROM ExerciseEntity e
            WHERE EXISTS (SELECT 1 FROM FavoriteExerciseEntity fe
                          WHERE fe.exercise.id = e.id AND fe.user.id = :userId)
            """)
    Page<Long> findPageIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
            SELECT e.id FROM ExerciseEntity e
            LEFT JOIN e.muscles m
            WHERE EXISTS (SELECT 1 FROM FavoriteExerciseEntity fe
                          WHERE fe.exercise.id = e.id AND fe.user.id = :userId)
            AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\'
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\')
            GROUP BY e.id, e.name
            ORDER BY e.name ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT e.id) FROM ExerciseEntity e
            LEFT JOIN e.muscles m
            WHERE EXISTS (SELECT 1 FROM FavoriteExerciseEntity fe
                          WHERE fe.exercise.id = e.id AND fe.user.id = :userId)
            AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\'
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\')
            """)
    Page<Long> findPageIdsByUserIdAndName(@Param("userId") Long userId, @Param("name") String name, Pageable pageable);

    @Query(value = """
            SELECT e.id FROM ExerciseEntity e
            JOIN e.muscles m
            WHERE EXISTS (SELECT 1 FROM FavoriteExerciseEntity fe
                          WHERE fe.exercise.id = e.id AND fe.user.id = :userId)
            AND m.id = :muscleId
            GROUP BY e.id, e.name
            ORDER BY e.name ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT e.id) FROM ExerciseEntity e
            JOIN e.muscles m
            WHERE EXISTS (SELECT 1 FROM FavoriteExerciseEntity fe
                          WHERE fe.exercise.id = e.id AND fe.user.id = :userId)
            AND m.id = :muscleId
            """)
    Page<Long> findPageIdsByUserIdAndMuscleId(@Param("userId") Long userId, @Param("muscleId") Long muscleId, Pageable pageable);

    @Query(value = """
            SELECT e.id FROM ExerciseEntity e
            JOIN e.muscles m
            WHERE EXISTS (SELECT 1 FROM FavoriteExerciseEntity fe
                          WHERE fe.exercise.id = e.id AND fe.user.id = :userId)
            AND m.id = :muscleId
            AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\'
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\')
            GROUP BY e.id, e.name
            ORDER BY e.name ASC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT e.id) FROM ExerciseEntity e
            JOIN e.muscles m
            WHERE EXISTS (SELECT 1 FROM FavoriteExerciseEntity fe
                          WHERE fe.exercise.id = e.id AND fe.user.id = :userId)
            AND m.id = :muscleId
            AND (LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\'
            OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\')
            """)
    Page<Long> findPageIdsByUserIdAndMuscleIdAndName(@Param("userId") Long userId, @Param("muscleId") Long muscleId, @Param("name") String name, Pageable pageable);
}
