package com.example.fitness_assistant.infrastructure.init;

import com.example.fitness_assistant.infrastructure.persistence.entity.ExerciseEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.FoodEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.MuscleEntity;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaExerciseRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaFoodRepository;
import com.example.fitness_assistant.infrastructure.persistence.jpa.JpaMuscleRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final JpaFoodRepository foodRepository;
    private final JpaMuscleRepository muscleRepository;
    private final JpaExerciseRepository exerciseRepository;

    @Override
    public void run(String... args) {
        if (foodRepository.count() > 0) {
            log.info("Данные уже загружены, инициализация пропущена");
            return;
        }

        loadFood();
        loadMuscles();
        loadExercises();
        log.info("Инициализация данных завершена");
    }

    private void loadFood() {
        List<FoodEntity> foods = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource("data/food.csv").getInputStream(), "UTF-8"))) {

            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 6) continue;

                String name = line[0].trim();
                String brands = line[1].trim();
                Double kcal = parseDouble(line[2]);
                Double proteins = parseDouble(line[3]);
                Double fats = parseDouble(line[4]);
                Double carbs = parseDouble(line[5]);

                FoodEntity food = new FoodEntity(null, name, brands.equals("ххх") ? null : brands,
                        kcal, proteins, fats, carbs);
                foods.add(food);
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Ошибка загрузки food.csv", e);
            return;
        }

        foodRepository.saveAll(foods);
        log.info("Загружено продуктов: {}", foods.size());
    }

    private void loadMuscles() {
        List<MuscleEntity> muscles = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource("data/muscles.csv").getInputStream(), "UTF-8"))) {

            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 1) continue;
                muscles.add(new MuscleEntity(null, line[0].trim()));
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Ошибка загрузки muscles.csv", e);
            return;
        }

        muscleRepository.saveAll(muscles);
        log.info("Загружено мышц: {}", muscles.size());
    }

    private void loadExercises() {
        Map<Long, MuscleEntity> muscleMap = new HashMap<>();
        for (MuscleEntity m : muscleRepository.findAll()) {
            muscleMap.put(m.getId(), m);
        }

        List<ExerciseEntity> exercises = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource("data/exercises.csv").getInputStream(), "UTF-8"))) {

            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 3) continue;

                String name = line[0].trim();
                String description = line[1].trim();
                String[] muscleIdStrings = line[2].split(",");

                List<MuscleEntity> exerciseMuscles = new ArrayList<>();
                for (String idStr : muscleIdStrings) {
                    Long id = parseLong(idStr);
                    if (id != null && muscleMap.containsKey(id)) {
                        exerciseMuscles.add(muscleMap.get(id));
                    }
                }

                exercises.add(new ExerciseEntity(null, name, description, exerciseMuscles));
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Ошибка загрузки exercises.csv", e);
            return;
        }

        exerciseRepository.saveAll(exercises);
        log.info("Загружено упражнений: {}", exercises.size());
    }

    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value.replace(",", ".").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private Long parseLong(String value) {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
