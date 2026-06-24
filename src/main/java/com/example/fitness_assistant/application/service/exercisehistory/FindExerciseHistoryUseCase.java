package com.example.fitness_assistant.application.service.exercisehistory;

import com.example.fitness_assistant.core.model.ExerciseHistory;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindExerciseHistoryUseCase {

    private final SetRepository setRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutRepository workoutRepository;

    @Transactional(readOnly = true)
    public List<ExerciseHistory> findExerciseHistory(Long exerciseId, Long userId, LocalDateTime from, LocalDateTime to) {
        List<Set> sets = setRepository.findByExerciseIdAndUserIdAndStartTimeBetween(exerciseId, userId, from, to);
        if (sets.isEmpty()) {
            log.info("История упражнения пуста | exerciseId={} | userId={}", exerciseId, userId);
            return List.of();
        }

        Map<Long, List<Set>> setsBySession = sets.stream()
                .collect(Collectors.groupingBy(Set::getSessionId));

        List<Long> sessionIds = setsBySession.keySet().stream().toList();
        List<WorkoutSession> sessions = workoutSessionRepository.findAllByIdIn(sessionIds);
        Map<Long, WorkoutSession> sessionMap = sessions.stream()
                .collect(Collectors.toMap(WorkoutSession::getId, s -> s));

        List<Long> workoutIds = sessions.stream()
                .map(WorkoutSession::getWorkoutId)
                .distinct()
                .toList();
        Map<Long, String> workoutNameMap = workoutRepository.findAllById(workoutIds).stream()
                .collect(Collectors.toMap(Workout::getId, Workout::getName));

        List<ExerciseHistory> history = sessionIds.stream()
                .map(sessionId -> {
                    WorkoutSession session = sessionMap.get(sessionId);
                    if (session == null) return null;
                    String workoutName = workoutNameMap.getOrDefault(session.getWorkoutId(), "Тренировка");
                    return new ExerciseHistory(
                            sessionId,
                            workoutName,
                            session.getStartTime(),
                            session.getEndTime(),
                            setsBySession.get(sessionId)
                    );
                })
                .filter(java.util.Objects::nonNull)
                .sorted(Comparator.comparing(ExerciseHistory::getStartTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        log.info("История упражнения загружена | exerciseId={} | userId={} | точек={}", exerciseId, userId, history.size());
        return history;
    }
}
