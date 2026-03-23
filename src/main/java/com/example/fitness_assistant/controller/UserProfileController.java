package com.example.fitness_assistant.controller;

import com.example.fitness_assistant.dto.UserProfileDTO;
import com.example.fitness_assistant.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "Профиль пользователя", description = "Управление персональными данными и параметрами тела")
@SecurityRequirement(name = "Bearer Authentication")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(
            summary = "Получить профиль",
            description = "Возвращает профиль текущего авторизованного пользователя. Email берётся из JWT-токена."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль получен"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
    @GetMapping
    public UserProfileDTO getProfile(Authentication auth) {
        return userProfileService.getProfile(auth.getName());
    }

    @Operation(
            summary = "Сохранить профиль",
            description = "Создаёт или полностью обновляет профиль пользователя. Вызывается после регистрации на странице setup-profile."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль сохранён"),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
    @PostMapping
    public UserProfileDTO saveProfile(Authentication auth,
                                      @RequestBody UserProfileDTO dto) {
        return userProfileService.saveProfile(auth.getName(), dto);
    }

    @Operation(
            summary = "Обновить вес и рост",
            description = """
                    Частичное обновление параметров тела. Принимает только weight и/или height.
                    Используется в модалке на dashboard без перезагрузки страницы.
                    
                    Пример тела запроса:
                    ```json
                    { "weight": 75.5, "height": 180.0 }
                    ```
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Параметры обновлены",
                    content = @Content(schema = @Schema(implementation = UserProfileDTO.class))),
            @ApiResponse(responseCode = "401", description = "Требуется авторизация")
    })
    @PatchMapping("/body")
    public UserProfileDTO updateBody(Authentication auth,
                                     @RequestBody
                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                             description = "Вес в кг и/или рост в см",
                                             content = @Content(schema = @Schema(example = "{\"weight\": 75.5, \"height\": 180.0}"))
                                     )
                                     Map<String, Double> body) {
        return userProfileService.updateWeightHeight(
                auth.getName(),
                body.get("weight"),
                body.get("height")
        );
    }
}