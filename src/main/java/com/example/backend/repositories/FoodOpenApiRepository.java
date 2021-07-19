package com.example.backend.repositories;

import com.example.backend.models.Foods;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodOpenApiRepository extends CrudRepository<Foods, Long> {
    @Query("SELECT COUNT(f) > 0 FROM Foods f WHERE f.foodName =:name AND f.category =:category")
    boolean existsByNameAndCategory(@Param("name") String name, @Param("category") String category);
}
