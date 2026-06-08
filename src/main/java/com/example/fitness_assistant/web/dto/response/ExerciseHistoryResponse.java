package com.example.fitness_assistant.web.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record ExerciseHistoryResponse(
        Long sessionId,
        String workoutName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<SetResponse> sets
) {}
