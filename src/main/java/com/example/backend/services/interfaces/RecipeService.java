package com.example.backend.services.interfaces;

import com.example.backend.models.Recipes;

import java.util.List;

public interface RecipeService {

    List<Recipes> recipeListExtractFromDB();

    Recipes termFrequencyInverseDocumentFrequency(double[] ingested, List<Recipes> recipesArrayList);

    void save(Recipes recipe);

    void delete(Recipes recipe);
}
