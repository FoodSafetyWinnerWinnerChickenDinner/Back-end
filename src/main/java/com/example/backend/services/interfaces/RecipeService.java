package com.example.backend.services.interfaces;

import com.example.backend.models.Recipes;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    List<Recipes> recipeListExtractFromDB();

    List<Optional> menuRecommendation(double[] ingested, List<Recipes> recipesArrayList, double responseSize);

    void save(Recipes recipe);

    void delete(Recipes recipe);
}
