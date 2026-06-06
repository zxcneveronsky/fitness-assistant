package com.example.fitness_assistant.web.controller;

import com.example.fitness_assistant.application.service.favoriteexercise.CreateFavoriteExerciseUseCase;
import com.example.fitness_assistant.application.service.favoriteexercise.DeleteFavoriteExerciseUseCase;
import com.example.fitness_assistant.application.service.favoriteexercise.FindFavoriteExerciseUseCase;
import com.example.fitness_assistant.application.service.favoriteexercise.GetFavoriteExerciseIdsUseCase;
import com.example.fitness_assistant.application.service.favoritefood.CreateFavoriteFoodUseCase;
import com.example.fitness_assistant.application.service.favoritefood.DeleteFavoriteFoodUseCase;
import com.example.fitness_assistant.application.service.favoritefood.FindFavoriteFoodUseCase;
import com.example.fitness_assistant.application.service.favoritefood.GetFavoriteFoodIdsUseCase;
import com.example.fitness_assistant.infrastructure.security.UserDetailsAdapter;
import com.example.fitness_assistant.web.dto.response.ExerciseResponse;
import com.example.fitness_assistant.web.dto.response.FavoriteExerciseResponse;
import com.example.fitness_assistant.web.dto.response.FavoriteFoodResponse;
import com.example.fitness_assistant.web.dto.response.FoodResponse;
import com.example.fitness_assistant.web.mapper.ExerciseWebMapper;
import com.example.fitness_assistant.web.mapper.FavoriteExerciseWebMapper;
import com.example.fitness_assistant.web.mapper.FavoriteFoodWebMapper;
import com.example.fitness_assistant.web.mapper.FoodWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
@Validated
public class FavoriteController {

    private final CreateFavoriteExerciseUseCase createFavoriteExerciseUseCase;
    private final DeleteFavoriteExerciseUseCase deleteFavoriteExerciseUseCase;
    private final FindFavoriteExerciseUseCase findFavoriteExerciseUseCase;
    private final GetFavoriteExerciseIdsUseCase getFavoriteExerciseIdsUseCase;
    private final CreateFavoriteFoodUseCase createFavoriteFoodUseCase;
    private final DeleteFavoriteFoodUseCase deleteFavoriteFoodUseCase;
    private final FindFavoriteFoodUseCase findFavoriteFoodUseCase;
    private final GetFavoriteFoodIdsUseCase getFavoriteFoodIdsUseCase;
    private final FavoriteExerciseWebMapper favoriteExerciseWebMapper;
    private final FavoriteFoodWebMapper favoriteFoodWebMapper;
    private final ExerciseWebMapper exerciseWebMapper;
    private final FoodWebMapper foodWebMapper;

    @PostMapping("/exercise/{exerciseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteExerciseResponse addFavoriteExercise(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable Long exerciseId) {
        return favoriteExerciseWebMapper.toResponse(
                createFavoriteExerciseUseCase.createFavorite(adapter.getUserId(), exerciseId)
        );
    }

    @DeleteMapping("/exercise/{exerciseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavoriteExercise(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable Long exerciseId) {
        deleteFavoriteExerciseUseCase.deleteFavorite(adapter.getUserId(), exerciseId);
    }

    @GetMapping("/exercise/ids")
    public List<Long> getFavoriteExerciseIds(
            @AuthenticationPrincipal UserDetailsAdapter adapter) {
        return getFavoriteExerciseIdsUseCase.getIds(adapter.getUserId());
    }

    @GetMapping("/exercise")
    public Page<ExerciseResponse> searchFavoriteExercises(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long muscleId,
            @PageableDefault(size = 12) Pageable pageable) {
        return findFavoriteExerciseUseCase.searchFavorites(adapter.getUserId(), name, muscleId, pageable)
                .map(exerciseWebMapper::toResponse);
    }

    @PostMapping("/food/{foodId}")
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteFoodResponse addFavoriteFood(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable Long foodId) {
        return favoriteFoodWebMapper.toResponse(
                createFavoriteFoodUseCase.createFavorite(adapter.getUserId(), foodId)
        );
    }

    @DeleteMapping("/food/{foodId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavoriteFood(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @PathVariable Long foodId) {
        deleteFavoriteFoodUseCase.deleteFavorite(adapter.getUserId(), foodId);
    }

    @GetMapping("/food/ids")
    public List<Long> getFavoriteFoodIds(
            @AuthenticationPrincipal UserDetailsAdapter adapter) {
        return getFavoriteFoodIdsUseCase.getIds(adapter.getUserId());
    }

    @GetMapping("/food")
    public Page<FoodResponse> searchFavoriteFoods(
            @AuthenticationPrincipal UserDetailsAdapter adapter,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 12) Pageable pageable) {
        return findFavoriteFoodUseCase.searchFavorites(adapter.getUserId(), name, pageable)
                .map(foodWebMapper::toResponse);
    }
}
