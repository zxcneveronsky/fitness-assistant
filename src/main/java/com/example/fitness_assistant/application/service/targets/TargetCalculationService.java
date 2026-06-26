package com.example.fitness_assistant.application.service.targets;

import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.model.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class TargetCalculationService {

    private static final double PROTEIN_RATIO = 0.30;
    private static final double FAT_RATIO = 0.30;
    private static final double CARB_RATIO = 0.40;
    private static final int KCAL_PER_G_PROTEIN = 4;
    private static final int KCAL_PER_G_FAT = 9;
    private static final int KCAL_PER_G_CARBS = 4;
    private static final double ACTIVITY_MULTIPLIER = 1.5;
    private static final double HYDRATION_MALE_RATE = 0.035;
    private static final double HYDRATION_FEMALE_RATE = 0.031;

    public void balanceMacrosByCalories(Targets targets) {
        Double kcal = targets.getTargetKcal();
        if (kcal == null) return;

        targets.setTargetProteins((kcal * PROTEIN_RATIO) / KCAL_PER_G_PROTEIN);
        targets.setTargetFats((kcal * FAT_RATIO) / KCAL_PER_G_FAT);
        targets.setTargetCarbs((kcal * CARB_RATIO) / KCAL_PER_G_CARBS);
    }

    public void balanceCaloriesByMacros(Targets targets) {
        Double proteins = targets.getTargetProteins();
        Double fats = targets.getTargetFats();
        Double carbs = targets.getTargetCarbs();

        if (proteins == null || fats == null || carbs == null) return;
        Double totalKcal = (proteins * KCAL_PER_G_PROTEIN) + (carbs * KCAL_PER_G_CARBS) + (fats * KCAL_PER_G_FAT);

        targets.setTargetKcal(totalKcal);
    }

    public void applyManualTargets(Targets targets, Targets request) {
        if (hasMacros(request)) {
            if (request.getTargetProteins() != null) {
                targets.setTargetProteins(request.getTargetProteins());
            }
            if (request.getTargetFats() != null) {
                targets.setTargetFats(request.getTargetFats());
            }
            if (request.getTargetCarbs() != null) {
                targets.setTargetCarbs(request.getTargetCarbs());
            }
            balanceCaloriesByMacros(targets);
        }
        else if (request.getTargetKcal() != null) {
            targets.setTargetKcal(request.getTargetKcal());
            balanceMacrosByCalories(targets);
        }
        if (request.getTargetHydration() != null) {
            targets.setTargetHydration(request.getTargetHydration());
        }
    }

    public void applyAutoTargets(Targets targets, UserProfile profile) {
        Double weight = profile.getWeight();
        Double height = profile.getHeight();
        UserProfile.Gender gender = profile.getGender();

        if (weight == null || height == null || gender == null) {
            log.warn("Недостаточно данных для авторасчёта: weight={}, height={}, gender={}", weight, height, gender);
            return;
        }

        Integer age = getAge(profile);
        Double kcal = 0.0;
        Double hydration = 0.0;
        if (gender == UserProfile.Gender.MALE) {
            kcal = (10 * weight) + (6.25 * height) - (5 * age) + 5;
            hydration = weight * HYDRATION_MALE_RATE;
        } else if (gender == UserProfile.Gender.FEMALE) {
            kcal = (10 * weight) + (6.25 * height) - (5 * age) - 161;
            hydration = weight * HYDRATION_FEMALE_RATE;
        }
        targets.setTargetKcal(kcal * ACTIVITY_MULTIPLIER);
        targets.setTargetHydration(hydration);
        balanceMacrosByCalories(targets);
    }

    public boolean hasMacros(Targets targets) {
        return targets.getTargetProteins() != null || targets.getTargetFats() != null || targets.getTargetCarbs() != null;
    }

    private Integer getAge(UserProfile profile) {
        if (profile.getBirthDate() == null) {
            log.warn("Дата рождения не указана, возраст = 0");
            return 0;
        }
        return Period.between(profile.getBirthDate(), LocalDate.now()).getYears();
    }
}
