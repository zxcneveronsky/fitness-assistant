package com.example.fitness_assistant.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSession {
    private Long id;
    private Long workoutId;
    private Long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private SessionStatus status;
}
