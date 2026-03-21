package com.example.fitness_assistant.init;

import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.repository.ExerciseMuscleRepository;
import com.example.fitness_assistant.repository.ExerciseRepository;
import com.example.fitness_assistant.repository.FoodRepository;
import com.opencsv.CSVReader;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Component
public class DataInitializer implements ApplicationRunner {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMuscleRepository exerciseMuscleRepository;
    private final FoodRepository foodRepository;

    public DataInitializer(ExerciseRepository exerciseRepository,
                           ExerciseMuscleRepository exerciseMuscleRepository,
                           FoodRepository foodRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMuscleRepository = exerciseMuscleRepository;
        this.foodRepository = foodRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadExercises();
        loadFood();
    }

    @Transactional
    public void loadExercises() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/exercises.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> lines = reader.readAll();
            lines.remove(0);

            long csvCount = lines.stream()
                    .map(line -> line[2])
                    .distinct()
                    .count();

            if (exerciseRepository.count() == csvCount) return;

            exerciseMuscleRepository.deleteAll();
            exerciseRepository.deleteAll();

            for (String[] line : lines) {
                String muscleGroup = line[0];
                String muscleDetail = line[1];
                String exerciseName = line[2];
                String description = line[3];

                Exercise exercise = exerciseRepository
                        .findByExerciseNameIgnoreCase(exerciseName)
                        .orElseGet(() -> {
                            Exercise newExercise = new Exercise();
                            newExercise.setExerciseName(exerciseName);
                            newExercise.setDescription(description);
                            return exerciseRepository.save(newExercise);
                        });

                ExerciseMuscle muscle = new ExerciseMuscle();
                muscle.setExercise(exercise);
                muscle.setMuscleGroup(muscleGroup);
                muscle.setMuscleDetail(muscleDetail);
                exerciseMuscleRepository.save(muscle);
            }
            log.info("Загружено упражнений: {}", exerciseRepository.count());
        }
    }

    public void loadFood() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/food.csv");

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            List<String[]> lines = reader.readAll();
            lines.remove(0);

            if (foodRepository.count() == lines.size()) return;

            foodRepository.deleteAll();

            for (String[] line : lines) {
                Food food = new Food();
                food.setName(line[0]);
                food.setBrands(line[1]);
                food.setKcal(Double.parseDouble(line[2]));
                food.setProteins(Double.parseDouble(line[3]));
                food.setFats(Double.parseDouble(line[4]));
                food.setCarbs(Double.parseDouble(line[5]));
                foodRepository.save(food);
            }
            log.info("Загружено продуктов: {}", foodRepository.count());
        }
    }
}