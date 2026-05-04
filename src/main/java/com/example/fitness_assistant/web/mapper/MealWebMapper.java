package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Meal;
import com.example.fitness_assistant.web.dto.request.create.CreateMealAutoRequest;
import com.example.fitness_assistant.web.dto.request.create.CreateMealManualRequest;
import com.example.fitness_assistant.web.dto.request.update.UpdateMealRequest;
import com.example.fitness_assistant.web.dto.response.MealResponse;
import org.springframework.stereotype.Component;

@Component
public class MealWebMapper {

    public Meal toDomain(CreateMealManualRequest request) {
        return new Meal(
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.name(),
                request.brands(),
                request.kcal(),
                request.proteins(),
                request.fats(),
                request.carbs(),
                request.consumedAt()
        );
    }

    public Meal toDomain(CreateMealAutoRequest request) {
        return new Meal(
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.consumedAt()
        );
    }

    public Meal toDomain(UpdateMealRequest request) {
        return new Meal(
                request.id(),
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.name(),
                request.brands(),
                request.kcal(),
                request.proteins(),
                request.fats(),
                request.carbs(),
                request.consumedAt()
        );
    }

    public MealResponse toResponse(Meal meal) {
        return new MealResponse(
                meal.getId(),
                meal.getName(),
                meal.getBrands(),
                meal.getKcal(),
                meal.getProteins(),
                meal.getFats(),
                meal.getCarbs(),
                meal.getConsumedAt()
        );
    }
}
