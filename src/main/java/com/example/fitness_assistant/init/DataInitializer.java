package com.example.fitness_assistant.init;

import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.repository.ExerciseRepository;
import com.example.fitness_assistant.repository.FoodRepository;
import com.opencsv.CSVReader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final ExerciseRepository exerciseRepository;
    private final FoodRepository foodRepository;

    public DataInitializer(ExerciseRepository exerciseRepository,FoodRepository foodRepository) {
        this.exerciseRepository = exerciseRepository;
        this.foodRepository = foodRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadExercises();
        loadFood();
    }

    private void loadExercises() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/exercises.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> lines = reader.readAll();
            lines.remove(0);

            if (exerciseRepository.count() == lines.size()) return;

            exerciseRepository.deleteAll();

            for (String[] line : lines) {
                Exercise exercise = new Exercise();
                exercise.setMuscleGroup(line[0]);
                exercise.setMuscleDetail(line[1]);
                exercise.setExerciseName(line[2]);
                exercise.setDescription(line[3]);
                exerciseRepository.save(exercise);
            }
        }
    }
    private void loadFood() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/food.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> lines = reader.readAll();
            lines.remove(0);

            if (foodRepository.count() == lines.size()) return;

            foodRepository.deleteAll();

            for (String[] line : lines) {
                Food food = new Food();
                food.setBarcode(line[0]);
                food.setName(line[1]);
                food.setBrands(line[2]);
                food.setKcal(Double.parseDouble(line[3]));
                food.setProteins(Double.parseDouble(line[4]));
                food.setFats(Double.parseDouble(line[5]));
                food.setCarbs(Double.parseDouble(line[6]));
                foodRepository.save(food);
            }
        }
    }

}