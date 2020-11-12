package com.example.backend.repositories;

import com.example.backend.models.Manuals;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManualRepository extends CrudRepository<Manuals, Long> {
    @Query("SELECT m FROM Manuals AS m, Recipes AS r WHERE r.id = ?1")
    public List<Manuals> findAllByRecipeId(Long recipeId);
}
