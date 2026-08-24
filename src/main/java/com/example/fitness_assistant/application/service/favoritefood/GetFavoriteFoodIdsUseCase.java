package com.example.fitness_assistant.application.service.favoritefood;

import com.example.fitness_assistant.core.repository.FavoriteFoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetFavoriteFoodIdsUseCase {

    private final FavoriteFoodRepository favoriteFoodRepository;

    @Transactional(readOnly = true)
    public List<Long> getFavoriteFoodIds(Long userId) {
        List<Long> ids = favoriteFoodRepository.findIdsByUserId(userId);
        log.info("Получены id избранных продуктов | userId={} | count={}", userId, ids.size());
        return ids;
    }
}
