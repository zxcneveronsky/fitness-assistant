package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.AccessDeniedException;
import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        userRepository.deleteById(userId);
        log.info("Пользователь удалён | id={}", userId);
    }

    @Transactional
    public void deleteUser(Long currentUserId, Long userId) {
        if (!currentUserId.equals(userId)) {
            throw new AccessDeniedException();
        }
        deleteUser(userId);
    }
}
