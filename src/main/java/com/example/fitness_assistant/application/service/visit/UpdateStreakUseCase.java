package com.example.fitness_assistant.application.service.visit;

import com.example.fitness_assistant.core.model.Streak;
import com.example.fitness_assistant.core.repository.StreakRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateStreakUseCase {

    private final StreakRepository streakRepository;

    @Transactional
    public Streak updateStreak(Long userId) {
        Streak streak = streakRepository.findById(userId)
                .orElse(new Streak(userId, 0, null));

        LocalDate today = LocalDate.now();
        LocalDate lastDate = streak.getLastVisitDate();

        if (lastDate == null) {
            streak.setStreak(0);
            streak.setLastVisitDate(today);
        } else if (lastDate.equals(today)) {
            log.info("Streak сегодня уже проверен | userId={} | streak={}", userId, streak.getStreak());
            return streak;
        } else if (lastDate.equals(today.minusDays(1))) {
            streak.setStreak(streak.getStreak() + 1);
            streak.setLastVisitDate(today);
        } else {
            streak.setStreak(0);
            streak.setLastVisitDate(today);
        }

        Streak savedStreak = streakRepository.save(streak);
        log.info("Streak обновлён | userId={} | streak={}", userId, savedStreak.getStreak());
        return savedStreak;
    }
}
