package com.example.fitness_assistant.application.service.targets;

import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.web.dto.request.update.UpdateTargetsRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class TargetCalculationService {

    public void balanceMacrosByCalories(UserProfile profile) {
        Double kcal = profile.getTargetKcal();

        profile.setTargetProteins((kcal * 0.30) / 4);
        profile.setTargetFats((kcal * 0.30) / 9);
        profile.setTargetCarbs((kcal * 0.40) / 4);
    }

    public void balanceCaloriesByMacros(UserProfile profile) {
        Double p = profile.getTargetProteins();
        Double f = profile.getTargetFats();
        Double c = profile.getTargetCarbs();

        Double totalKcal = (p * 4) + (c * 4) + (f * 9);

        profile.setTargetKcal(totalKcal);
    }
    public void applyManualTargets(UserProfile profile, UpdateTargetsRequest request) {
        if (hasMacros(request)) {
            if (request.targetProteins() != null) profile.setTargetProteins(request.targetProteins());
            if (request.targetFats() != null) profile.setTargetFats(request.targetFats());
            if (request.targetCarbs() != null) profile.setTargetCarbs(request.targetCarbs());
            balanceCaloriesByMacros(profile);
        }
        else if (request.targetKcal() != null) {
            profile.setTargetKcal(request.targetKcal());
            balanceMacrosByCalories(profile);
        }
    }
    public void applyAutoTargets(UserProfile profile) {
        Integer age = getAge(profile);
        UserProfile.Gender gender = profile.getGender();
        Double weight = profile.getWeight();
        Double height = profile.getHeight();
        Double kcal=0.0;
        if (gender == UserProfile.Gender.MALE) {
            kcal = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else if (gender == UserProfile.Gender.FEMALE) {
            kcal = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }
        profile.setTargetKcal(kcal*1.5);
        balanceMacrosByCalories(profile);
    }
    public boolean hasMacros(UpdateTargetsRequest r) {
        return r.targetProteins() != null || r.targetFats() != null || r.targetCarbs() != null;
    }
    private Integer getAge(UserProfile profile) {
        return Period.between(profile.getBirthDate(), LocalDate.now()).getYears();
    }
}

