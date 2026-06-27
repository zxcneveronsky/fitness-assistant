package com.example.fitness_assistant.application.service.workoutaccess;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.exception.WorkoutAccessAlreadyExistsException;
import com.example.fitness_assistant.core.exception.WorkoutNotFoundException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.model.workoutaccess.AccessLevel;
import com.example.fitness_assistant.core.model.workoutaccess.WorkoutAccess;
import com.example.fitness_assistant.core.repository.UserRepository;
import com.example.fitness_assistant.core.repository.WorkoutAccessRepository;
import com.example.fitness_assistant.core.repository.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateWorkoutAccessUseCase {

    private final WorkoutAccessRepository workoutAccessRepository;
    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkoutAccess createWorkoutAccess(Long userId, Long workoutId, String email, AccessLevel accessLevel) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        if (!workoutRepository.existsById(workoutId, userId)) {
            throw new WorkoutNotFoundException(workoutId);
        }
        User sharedWithUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        Long sharedWithUserId = sharedWithUser.getId();
        if (workoutAccessRepository.existsByOwnerIdAndSharedWithUserIdAndWorkoutId(userId, sharedWithUserId, workoutId)) {
            throw new WorkoutAccessAlreadyExistsException(workoutId);
        }
        WorkoutAccess savedWorkoutAccess = workoutAccessRepository.save(
                new WorkoutAccess(null, userId, sharedWithUserId, workoutId, null, accessLevel)
        );
        log.info("Доступ к тренировке создан | id={} | workoutId={} | sharedWithUserId={} | accessLevel={}",
                savedWorkoutAccess.getId(), workoutId, sharedWithUserId, accessLevel);
        return savedWorkoutAccess;
    }
}
