package com.example.fitness_assistant.init;

import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.repository.ExerciseMuscleRepository;
import com.example.fitness_assistant.repository.ExerciseRepository;
import com.example.fitness_assistant.repository.FoodRepository;
import com.opencsv.CSVReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMuscleRepository exerciseMuscleRepository;
    private final FoodRepository foodRepository;
    private final JdbcTemplate jdbcTemplate;

    @Value("${app.init-data.directory}")
    private String dataDir;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (foodRepository.count() == 0 || exerciseRepository.count() == 0) {
            truncateTables();
            loadFoods();
            loadExercises();
            log.info("Загрузка данных завершена успешно");
        }
    }

    private void truncateTables() {
        jdbcTemplate.execute("TRUNCATE TABLE exercise_muscle RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE exercise RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE food RESTART IDENTITY CASCADE");
        log.info("Таблицы очищены.Индексация обнулена.");
    }

    @Transactional
    private void loadFoods() throws Exception {
        List<String[]> lines = readCsv(dataDir + "food.csv");
        if (lines.isEmpty()) return;
        lines.remove(0);

        List<Food> foods = new ArrayList<>();
        for (String[] line : lines) {
            if (line.length < 6) continue;

            Food food = new Food();
            food.setName(line[0].trim());
            food.setBrands(line[1].trim());
            food.setKcal(parse(line[2]));
            food.setProteins(parse(line[3]));
            food.setFats(parse(line[4]));
            food.setCarbs(parse(line[5]));
            foods.add(food);
        }

        foodRepository.saveAll(foods);
        log.info("Загружено продуктов: {}", foods.size());
    }

    @Transactional
    private void loadExercises() throws Exception {
        List<String[]> lines = readCsv(dataDir + "exercises.csv");
        if (lines.isEmpty()) return;
        lines.remove(0);

        List<String[]> csvData = new ArrayList<>(lines);
        Map<String, Exercise> exerciseMap = new HashMap<>();

        for (String[] line : csvData) {
            if (line.length < 4) continue;
            String name = line[2].trim();
            String desc = line[3].trim();

            exerciseMap.computeIfAbsent(name, k -> {
                Exercise ex = new Exercise();
                ex.setExerciseName(k);
                ex.setDescription(desc);
                return ex;
            });
        }

        List<Exercise> savedExercises = exerciseRepository.saveAll(exerciseMap.values());

        Map<String, Exercise> nameToSavedExercise = new HashMap<>();
        savedExercises.forEach(ex -> nameToSavedExercise.put(ex.getExerciseName(), ex));

        List<ExerciseMuscle> muscles = new ArrayList<>();
        for (String[] line : csvData) {
            if (line.length < 4) continue;
            String group = line[0].trim();
            String detail = line[1].trim();
            String name = line[2].trim();

            ExerciseMuscle muscle = new ExerciseMuscle();
            muscle.setExercise(nameToSavedExercise.get(name));
            muscle.setMuscleGroup(group);
            muscle.setMuscleDetail(detail);
            muscles.add(muscle);
        }

        exerciseMuscleRepository.saveAll(muscles);
        log.info("Упражнений: {}, связей с мышцами: {}",
                nameToSavedExercise.size(), muscles.size());
    }

    private List<String[]> readCsv(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            return reader.readAll();
        }
    }

    private double parse(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}