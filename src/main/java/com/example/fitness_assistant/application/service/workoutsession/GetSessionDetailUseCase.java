package com.example.fitness_assistant.application.service.workoutsession;

import com.example.fitness_assistant.core.exception.WorkoutSessionNotFoundException;
import com.example.fitness_assistant.core.model.WorkoutSession;
import com.example.fitness_assistant.core.model.sessiondetail.ExerciseDetail;
import com.example.fitness_assistant.core.model.sessiondetail.SessionDetail;
import com.example.fitness_assistant.core.model.sessiondetail.SetDetail;
import com.example.fitness_assistant.core.repository.SetRepository;
import com.example.fitness_assistant.core.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetSessionDetailUseCase {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final SetRepository setRepository;

    @Transactional(readOnly = true)
    public SessionDetail getSessionDetail(Long userId, Long sessionId) {
        WorkoutSession session = workoutSessionRepository.findById(sessionId, userId)
                .orElseThrow(() -> new WorkoutSessionNotFoundException(sessionId));

        List<SetDetail> sets = setRepository.findAllSetDetailBySessionId(sessionId);

        List<ExerciseDetail> exercises = sets.stream()
                .collect(Collectors.groupingBy(
                        set -> new ExerciseDetailKey(set.getExerciseId(), set.getName()),
                        LinkedHashMap::new,
                        Collectors.toList()
                ))
                .entrySet().stream()
                .map(entry -> new ExerciseDetail(
                        entry.getKey().exerciseId(),
                        entry.getKey().name(),
                        entry.getValue()
                ))
                .toList();

        log.info("Детали сессии загружены | id={} | упражнений={}", sessionId, exercises.size());
        return new SessionDetail(session.getId(), session.getStartTime(), session.getEndTime(), exercises);
    }

    private record ExerciseDetailKey(Long exerciseId, String name) {}
}
