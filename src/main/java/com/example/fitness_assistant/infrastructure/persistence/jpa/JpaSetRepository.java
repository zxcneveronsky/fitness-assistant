package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.SetEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaSetRepository extends JpaRepository<SetEntity, Long> {

    Page<SetEntity> findBySessionIdAndExerciseIdOrderByIdAsc(Long sessionId, Long exerciseId, Pageable pageable);

    @Query("""
            SELECT s FROM SetEntity s
            JOIN FETCH s.exercise e
            WHERE s.session.id = :sessionId
            ORDER BY e.id ASC, s.id ASC
            """)
    List<SetEntity> findAllWithExerciseBySessionId(@Param("sessionId") Long sessionId);

    @Query("""
            SELECT s FROM SetEntity s
            JOIN FETCH s.session sess
            WHERE s.exercise.id = :exerciseId
            AND sess.user.id = :userId
            AND sess.startTime BETWEEN :from AND :to
            ORDER BY sess.startTime DESC, s.id ASC
            """)
    Page<SetEntity> findByExerciseIdAndUserIdAndStartTimeBetween(
            @Param("exerciseId") Long exerciseId,
            @Param("userId") Long userId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);

    @Query("""
            SELECT s FROM SetEntity s
            JOIN FETCH s.session sess
            JOIN FETCH s.exercise ex
            WHERE s.id = :id
            AND sess.id = :sessionId
            AND sess.user.id = :userId
            """)
    Optional<SetEntity> findByIdAndSessionIdAndUserId(@Param("id") Long id,
                                                      @Param("sessionId") Long sessionId,
                                                      @Param("userId") Long userId);

    @Modifying
    @Query("""
            DELETE FROM SetEntity s
            WHERE s.id = :id
            AND s.session.id = :sessionId
            AND s.session.user.id = :userId
            """)
    long deleteByIdAndSessionIdAndUserId(@Param("id") Long id,
                                         @Param("sessionId") Long sessionId,
                                         @Param("userId") Long userId);
}
