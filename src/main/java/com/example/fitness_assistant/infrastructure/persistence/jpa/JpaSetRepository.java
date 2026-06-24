package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.SetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaSetRepository extends JpaRepository<SetEntity, Long> {
    @EntityGraph(attributePaths = {"session", "exercise"})
    Optional<SetEntity> findByIdAndSessionId(Long id, Long sessionId);
    void deleteByIdAndSessionId(Long id, Long sessionId);
    boolean existsByIdAndSessionId(Long id, Long sessionId);
    @Query("SELECT s FROM SetEntity s WHERE s.session.id = :sessionId AND s.exercise.id = :exerciseId ORDER BY s.id ASC")
    Page<SetEntity> findAllBySessionIdAndExerciseId(@Param("sessionId") Long sessionId, @Param("exerciseId") Long exerciseId, Pageable pageable);

    @Query("SELECT s FROM SetEntity s " +
           "JOIN FETCH s.exercise e " +
           "WHERE s.session.id = :sessionId " +
           "ORDER BY e.id ASC, s.id ASC")
    List<SetEntity> findAllWithExerciseBySessionId(@Param("sessionId") Long sessionId);

    @Query("SELECT s FROM SetEntity s " +
           "JOIN FETCH s.session sess " +
           "JOIN FETCH sess.workout w " +
           "WHERE s.exercise.id = :exerciseId " +
           "AND sess.user.id = :userId " +
           "AND sess.startTime BETWEEN :from AND :to " +
           "ORDER BY sess.startTime DESC, s.id ASC")
    List<SetEntity> findByExerciseIdAndUserIdAndStartTimeBetween(
            @Param("exerciseId") Long exerciseId,
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
