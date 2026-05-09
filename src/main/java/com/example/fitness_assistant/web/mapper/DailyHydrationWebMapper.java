package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.hydration.DailyHydration;
import com.example.fitness_assistant.web.dto.response.hydration.DailyHydrationResponse;
import org.springframework.stereotype.Component;

@Component
public class DailyHydrationWebMapper {
    public DailyHydrationResponse toResponse(DailyHydration dailyHydration){
        return new DailyHydrationResponse(
                dailyHydration.getTotalAmount()
        );
    }

}
