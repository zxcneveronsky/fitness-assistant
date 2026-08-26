package com.example.fitness_assistant.application.service.exercise;

import com.example.fitness_assistant.core.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.core.exception.InvalidDateRangeException;
import com.example.fitness_assistant.core.model.exercise.ExerciseHistory;
import com.example.fitness_assistant.core.model.Set;
import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.model.workout.Workout;
import com.example.fitness_assistant.core.repository.ExerciseRepository;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindExerciseHistoryUseCase {

    private final SetRepository setRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutRepository workoutRepository;

    @Transactional(readOnly = true)
    public Page<ExerciseHistory> findExerciseHistory(Long userId, Long exerciseId, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        if (from.isAfter(to)) {
            throw new InvalidDateRangeException();
        }
        if (!exerciseRepository.existsById(exerciseId)) {
            throw new ExerciseNotFoundException(exerciseId);
        }
        Page<Set> setsPage = setRepository.findByExerciseIdAndUserIdAndStartTimeBetween(exerciseId, userId, from, to, pageable);
        if (setsPage.isEmpty()) {
            log.info("История упражнения пуста | exerciseId={} | userId={}", exerciseId, userId);
            return Page.empty();
        }

        Map<Long, List<Set>> setsBySession = setsPage.getContent().stream()
                .collect(Collectors.groupingBy(Set::getSessionId));

        List<WorkoutSession> sessions = workoutSessionRepository.findAllByIdIn(setsBySession.keySet().stream().toList());
        Map<Long, WorkoutSession> sessionMap = sessions.stream()
                .collect(Collectors.toMap(WorkoutSession::getId, s -> s));

        List<Long> workoutIds = sessions.stream()
                .map(WorkoutSession::getWorkoutId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, String> workoutNameMap = workoutRepository.findAllAccessibleByIdIn(workoutIds, userId).stream()
                .collect(Collectors.toMap(Workout::getId, Workout::getName));

        List<ExerciseHistory> history = sessions.stream()
                .map(s -> new ExerciseHistory(
                        s.getId(),
                        workoutNameMap.getOrDefault(s.getWorkoutId(), "Тренировка"),
                        s.getStartTime(),
                        s.getEndTime(),
                        setsBySession.get(s.getId())
                ))
                .sorted(Comparator.comparing(ExerciseHistory::getStartTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();

        log.info("История упражнения загружена | exerciseId={} | userId={} | точек={}", exerciseId, userId, history.size());
        return new PageImpl<>(history, pageable, setsPage.getTotalElements());
    }
}
