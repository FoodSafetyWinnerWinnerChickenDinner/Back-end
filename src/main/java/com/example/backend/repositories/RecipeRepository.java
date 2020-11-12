package com.example.backend.repositories;

import com.example.backend.models.Recipes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends CrudRepository<Recipes, Long> {
}
