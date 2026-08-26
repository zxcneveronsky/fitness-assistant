package com.example.fitness_assistant.infrastructure.persistence.jpa;

import com.example.fitness_assistant.infrastructure.persistence.entity.FavoriteFoodEntity;
import com.example.fitness_assistant.infrastructure.persistence.entity.FoodEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaFavoriteFoodRepository extends JpaRepository<FavoriteFoodEntity, Long> {

    boolean existsByFoodIdAndUserId(Long foodId, Long userId);

    @Modifying
    @Query("DELETE FROM FavoriteFoodEntity ff WHERE ff.food.id = :foodId AND ff.user.id = :userId")
    long deleteByFoodIdAndUserId(@Param("foodId") Long foodId, @Param("userId") Long userId);

    @Query("""
            SELECT f FROM FoodEntity f
            WHERE EXISTS (SELECT 1 FROM FavoriteFoodEntity ff
                          WHERE ff.food.id = f.id AND ff.user.id = :userId)
            ORDER BY f.name ASC
            """)
    Page<FoodEntity> findByUserIdOrderByNameAsc(@Param("userId") Long userId, Pageable pageable);

    @Query("""
            SELECT f FROM FoodEntity f
            WHERE EXISTS (SELECT 1 FROM FavoriteFoodEntity ff
                          WHERE ff.food.id = f.id AND ff.user.id = :userId)
            AND (LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\'
            OR LOWER(f.brands) LIKE LOWER(CONCAT('%', :name, '%')) ESCAPE '\\')
            ORDER BY f.name ASC
            """)
    Page<FoodEntity> searchByNameAndUserId(@Param("name") String name, @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT ff.food.id FROM FavoriteFoodEntity ff WHERE ff.user.id = :userId ORDER BY ff.food.id ASC")
    List<Long> findIdsByUserId(@Param("userId") Long userId);
}
