package com.example.backend.repositories;

import com.example.backend.models.Recipes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeOpenApiRepository extends CrudRepository<Recipes, Long> {
    @Query("SELECT r from Recipes as r WHERE r.recipeName = ?1")
    Recipes findByName(String name);
}
