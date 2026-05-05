package com.example.fitness_assistant.web.mapper;

import com.example.fitness_assistant.core.model.Targets;
import com.example.fitness_assistant.core.model.UserProfile;
import com.example.fitness_assistant.web.dto.request.update.UpdateTargetsRequest;
import com.example.fitness_assistant.web.dto.response.TargetStatusResponse;
import com.example.fitness_assistant.web.dto.response.TargetsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TargetsWebMapper {

    public Targets toDomain(UpdateTargetsRequest request){
        return new Targets(
                request.targetKcal(),
                request.targetProteins(),
                request.targetFats(),
                request.targetCarbs(),
                null // Ставим null, потому что в UseCase проставляется false
                ,request.targetHydration()
        );
    }


    public TargetsResponse toTargetsResponse(UserProfile profile){
        return new TargetsResponse(
                profile.getTargetKcal(),
                profile.getTargetProteins(),
                profile.getTargetFats(),
                profile.getTargetCarbs(),
                profile.getTargetHydration()
        );
    }
    public TargetStatusResponse toTargetStatusResponse(UserProfile profile){
        return new TargetStatusResponse(
                profile.getUseAutopilot()
        );
    }

}
