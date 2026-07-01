package com.example.fitness_assistant.application.service.user;

import com.example.fitness_assistant.core.exception.UserNotFoundException;
import com.example.fitness_assistant.core.model.User;
import com.example.fitness_assistant.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindUserUseCase {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<User> findAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        log.info("Поиск пользователей завершён | найдено={} | страница={}/{}",
                users.getTotalElements(), users.getNumber() + 1, users.getTotalPages());
        return users;
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        log.info("Пользователь найден | id={}", userId);
        return user;
    }
}
