package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Streak;
import com.example.fitness_assistant.web.dto.response.StreakResponse;
import org.springframework.stereotype.Component;

@Component
public class StreakWebMapper {

    public StreakResponse toResponse(Streak streak) {
        return new StreakResponse(streak.getStreak());
    }
}
