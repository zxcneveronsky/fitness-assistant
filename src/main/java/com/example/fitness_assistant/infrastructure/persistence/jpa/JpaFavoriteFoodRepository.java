package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.FavoriteFoodEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.FoodEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaFavoriteFoodRepository extends JpaRepository<FavoriteFoodEntity, Long> {

    boolean existsByUserIdAndFoodId(Long userId, Long foodId);

    void deleteByUserIdAndFoodId(Long userId, Long foodId);

    @Query(value = """
        SELECT f FROM FoodEntity f
        WHERE f.id IN (SELECT ff.food.id FROM FavoriteFoodEntity ff WHERE ff.user.id = :userId)
        AND (cast(:name as text) IS NULL
        OR LOWER(f.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%'))
        OR LOWER(f.brands) LIKE LOWER(CONCAT('%', cast(:name as text), '%')))
        """,
            countQuery = """
        SELECT COUNT(f) FROM FoodEntity f
        WHERE f.id IN (SELECT ff.food.id FROM FavoriteFoodEntity ff WHERE ff.user.id = :userId)
        AND (cast(:name as text) IS NULL
        OR LOWER(f.name) LIKE LOWER(CONCAT('%', cast(:name as text), '%'))
        OR LOWER(f.brands) LIKE LOWER(CONCAT('%', cast(:name as text), '%')))
        """)
    Page<FoodEntity> searchFavoriteFoods(@Param("userId") Long userId, @Param("name") String name, Pageable pageable);

    @Query("SELECT ff.food.id FROM FavoriteFoodEntity ff WHERE ff.user.id = :userId")
    List<Long> findIdsByUserId(@Param("userId") Long userId);
}
