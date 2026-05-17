package com.example.fitness_assistant.application.service.targets;

import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.model.UserProfile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class TargetCalculationService {

    public void balanceMacrosByCalories(Targets targets) {
        Double kcal = targets.getTargetKcal();

        targets.setTargetProteins((kcal * 0.30) / 4);
        targets.setTargetFats((kcal * 0.30) / 9);
        targets.setTargetCarbs((kcal * 0.40) / 4);
    }

    public void balanceCaloriesByMacros(Targets targets) {
        Double p = targets.getTargetProteins();
        Double f = targets.getTargetFats();
        Double c = targets.getTargetCarbs();

        Double totalKcal = (p * 4) + (c * 4) + (f * 9);

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
        Integer age = getAge(profile);
        UserProfile.Gender gender = profile.getGender();
        Double weight = profile.getWeight();
        Double height = profile.getHeight();
        Double kcal = 0.0;
        Double hydration = 0.0;
        if (gender == UserProfile.Gender.MALE) {
            kcal = (10 * weight) + (6.25 * height) - (5 * age) + 5;
            hydration = weight * 0.035;
        } else if (gender == UserProfile.Gender.FEMALE) {
            kcal = (10 * weight) + (6.25 * height) - (5 * age) - 161;
            hydration = weight * 0.031;
        }
        targets.setTargetKcal(kcal*1.5);
        targets.setTargetHydration(hydration);
        balanceMacrosByCalories(targets);
    }

    public boolean hasMacros(Targets r) {
        return r.getTargetProteins() != null || r.getTargetFats() != null || r.getTargetCarbs() != null;
    }

    private Integer getAge(UserProfile profile) {
        return Period.between(profile.getBirthDate(), LocalDate.now()).getYears();
    }
}
