package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.web.dto.request.update.UpdateTargetsRequest;
import com.example.fitness_assistant.web.dto.response.targets.TargetStatusResponse;
import com.example.fitness_assistant.web.dto.response.targets.TargetsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TargetsWebMapper {

    public Targets toDomain(UpdateTargetsRequest request) {
        return new Targets(
                null, // Этого поля нет в запросе, поэтому проставляем null
                request.targetKcal(),
                request.targetProteins(),
                request.targetFats(),
                request.targetCarbs(),
                request.targetHydration(),
                null // Ставим null, потому что в UseCase проставляется false
        );
    }

    public TargetsResponse toTargetsResponse(Targets targets) {
        return new TargetsResponse(
                targets.getTargetKcal(),
                targets.getTargetProteins(),
                targets.getTargetFats(),
                targets.getTargetCarbs(),
                targets.getTargetHydration()
        );
    }

    public TargetStatusResponse toTargetStatusResponse(Targets targets) {
        return new TargetStatusResponse(
                targets.getUseAutopilot()
        );
    }
}
