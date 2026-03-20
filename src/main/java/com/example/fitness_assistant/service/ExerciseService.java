package com.example.fitness_assistant.service;


import com.example.fitness_assistant.dto.ExerciseDTO;
import com.example.fitness_assistant.entity.Exercise;
import com.example.fitness_assistant.exception.ExerciseNotFoundException;
import com.example.fitness_assistant.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;
    public ExerciseService(ExerciseRepository exerciseRepository){
        this.exerciseRepository = exerciseRepository; }

    private List<ExerciseDTO> toDTO(List<Exercise> ex){
        return ex.stream()
                .map(e -> new ExerciseDTO(
                        e.getExerciseName(),
                        e.getDescription(),
                        List.of(new ExerciseDTO.MuscleDTO(e.getMuscleGroup(),e.getMuscleDetail()))
                ))
                .toList();
    }

    public List<ExerciseDTO> getAllExercise(){
        List<Exercise> exercises = exerciseRepository.findAll();
        if (exercises.isEmpty()){
            log.warn("Список упражнений пуст");
            throw new ExerciseNotFoundException("Ничего");
        }
        log.debug("Успешно извлечено упражнений: {}", exercises.size());
        return toDTO(exercises);
    }
    public Page<ExerciseDTO> getAllExercisePaged(Pageable pageable) {
        Page<ExerciseDTO> result = exerciseRepository.findAll(pageable)
                .map(exercise -> new ExerciseDTO(
                        exercise.getExerciseName(),
                        exercise.getDescription(),
                        List.of(new ExerciseDTO.MuscleDTO(exercise.getMuscleGroup(), exercise.getMuscleDetail()))
                ));
        if (result.isEmpty()){
            log.warn("Страница {} пуста (запрос пагинации)", pageable.getPageNumber());
            throw new ExerciseNotFoundException("Ничего");
        }
        log.debug("Возвращена страница {} из {}. Найдено элементов: {}",
                result.getNumber(), result.getTotalPages(), result.getNumberOfElements());
        return result;
    }

    public List<ExerciseDTO> getExerciseName(String muscle){
        List<Exercise> exercisesName = exerciseRepository.findByMuscleGroupOrMuscleDetail(muscle,muscle);
        if (exercisesName.isEmpty()){
            log.warn("Не найдено упражнений для группы мышц: {}", muscle);
            throw new ExerciseNotFoundException(muscle);
        }
        log.debug("Найдено {} упражнений для группы: {}", exercisesName.size(), muscle);
        return toDTO(exercisesName);
    }

    public ExerciseDTO getMuscleName(String exerciseName){
        List<Exercise> muscleName = exerciseRepository.findByExerciseName(exerciseName);
        if (muscleName.isEmpty()){
            log.warn("Упражнение '{}' не найдено в базе", exerciseName);
            throw new ExerciseNotFoundException(exerciseName);
        }
        ExerciseDTO resultDTO = new ExerciseDTO(
                muscleName.get(0).getExerciseName(),
                muscleName.get(0).getDescription(),
                muscleName.stream()
                        .map(m -> new ExerciseDTO.MuscleDTO(m.getMuscleGroup(),m.getMuscleDetail()))
                        .toList()
        );
        log.debug("Сформированы данные для упражнения: {}, мышц: {}",
                resultDTO.exerciseName(), resultDTO.muscles().size());
        return resultDTO;
    }

    @Transactional
    public void deleteExercise(String exerciseName) {
        log.info("Выполнение удаления упражнения: {}", exerciseName);
        exerciseRepository.deleteByExerciseName(exerciseName);
    }
    public Exercise addExercise(Exercise exercise){
        Exercise exerciseSaved = exerciseRepository.save(exercise);
        log.info("Сохранено новое упражнение [ID: {}, Название: {}]", exerciseSaved.getId(), exerciseSaved.getExerciseName());
        return exerciseSaved;
    }
}
