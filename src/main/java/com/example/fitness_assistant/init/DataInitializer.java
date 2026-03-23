package com.example.fitness_assistant.init;

import com.example.fitness_assistant.entity.AppMetadata;
import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.entity.ExerciseMuscle;
import com.example.fitness_assistant.entity.Food;
import com.example.fitness_assistant.repository.AppMetadataRepository;
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

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;

@Slf4j
@Component
public class DataInitializer implements ApplicationRunner {

    private static final String KEY_EXERCISES = "csv.checksum.exercises";
    private static final String KEY_FOOD      = "csv.checksum.food";

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMuscleRepository exerciseMuscleRepository;
    private final FoodRepository foodRepository;
    private final AppMetadataRepository metadataRepository;

    public DataInitializer(ExerciseRepository exerciseRepository,
                           ExerciseMuscleRepository exerciseMuscleRepository,
                           FoodRepository foodRepository,
                           AppMetadataRepository metadataRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMuscleRepository = exerciseMuscleRepository;
        this.foodRepository = foodRepository;
        this.metadataRepository = metadataRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadExercises();
        loadFood();
    }

    @Transactional
    public void loadExercises() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/exercises.csv");
        byte[] csvBytes = resource.getInputStream().readAllBytes();
        String csvChecksum = String.valueOf(calcChecksum(csvBytes));

        String storedChecksum = metadataRepository.findById(KEY_EXERCISES)
                .map(AppMetadata::getValue)
                .orElse(null);

        if (csvChecksum.equals(storedChecksum)) {
            log.info("Упражнения актуальны, пропускаем загрузку (checksum={})", csvChecksum);
            return;
        }

        log.info("CSV упражнений изменился, перезагружаем... (было={}, стало={})",
                storedChecksum, csvChecksum);

        exerciseMuscleRepository.deleteAll();
        exerciseRepository.deleteAll();

        List<String[]> lines;
        try (CSVReader reader = new CSVReader(
                new InputStreamReader(new ByteArrayInputStream(csvBytes)))) {
            lines = reader.readAll();
        }
        lines.remove(0);

        List<ExerciseMuscle> musclesToSave = new ArrayList<>();

        for (String[] line : lines) {
            String muscleGroup  = line[0];
            String muscleDetail = line[1];
            String exerciseName = line[2];
            String description  = line[3];

            Exercise exercise = exerciseRepository
                    .findByExerciseNameIgnoreCase(exerciseName)
                    .orElseGet(() -> {
                        Exercise e = new Exercise();
                        e.setExerciseName(exerciseName);
                        e.setDescription(description);
                        return exerciseRepository.save(e);
                    });

            ExerciseMuscle muscle = new ExerciseMuscle();
            muscle.setExercise(exercise);
            muscle.setMuscleGroup(muscleGroup);
            muscle.setMuscleDetail(muscleDetail);
            musclesToSave.add(muscle);
        }

        exerciseMuscleRepository.saveAll(musclesToSave);
        metadataRepository.save(new AppMetadata(KEY_EXERCISES, csvChecksum));
        log.info("Загружено упражнений: {}, checksum={}", exerciseRepository.count(), csvChecksum);
    }

    @Transactional
    public void loadFood() throws Exception {
        ClassPathResource resource = new ClassPathResource("data/food.csv");
        byte[] csvBytes = resource.getInputStream().readAllBytes();
        String csvChecksum = String.valueOf(calcChecksum(csvBytes));

        String storedChecksum = metadataRepository.findById(KEY_FOOD)
                .map(AppMetadata::getValue)
                .orElse(null);

        if (csvChecksum.equals(storedChecksum)) {
            log.info("Продукты актуальны, пропускаем загрузку (checksum={})", csvChecksum);
            return;
        }

        log.info("CSV продуктов изменился, перезагружаем... (было={}, стало={})",
                storedChecksum, csvChecksum);

        foodRepository.deleteAll();

        List<String[]> lines;
        try (CSVReader reader = new CSVReader(
                new InputStreamReader(new ByteArrayInputStream(csvBytes)))) {
            lines = reader.readAll();
        }
        lines.removeFirst();

        List<Food> foods = lines.stream().map(line -> {
            Food food = new Food();
            food.setName(line[0]);
            food.setBrands(line[1]);
            food.setKcal(Double.parseDouble(line[2]));
            food.setProteins(Double.parseDouble(line[3]));
            food.setFats(Double.parseDouble(line[4]));
            food.setCarbs(Double.parseDouble(line[5]));
            return food;
        }).toList();

        foodRepository.saveAll(foods);
        metadataRepository.save(new AppMetadata(KEY_FOOD, csvChecksum));
        log.info("Загружено продуктов: {}, checksum={}", foodRepository.count(), csvChecksum);
    }

    private long calcChecksum(byte[] data) {
        CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }
}