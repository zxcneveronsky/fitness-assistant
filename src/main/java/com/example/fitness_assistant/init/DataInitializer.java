package com.example.fitness_assistant.init;

import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.repository.ExerciseRepository;
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

    public DataInitializer(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadExercises();
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
}