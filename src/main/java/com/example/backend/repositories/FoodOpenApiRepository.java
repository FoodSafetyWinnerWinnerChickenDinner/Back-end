package com.example.backend.repositories;

import com.example.backend.models.Foods;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodOpenApiRepository extends CrudRepository<Foods, Long> {
    @Query("SELECT f from Foods as f WHERE f.foodName = ?1 AND f.category = ?2 AND f.total = ?3")
    Foods findByNameAndCategory(String name, String category, double total);
}
