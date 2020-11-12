package com.example.backend.services.interfaces;

import com.example.backend.models.Recipes;

import java.net.MalformedURLException;

public interface RecipeOpenApiService {
    void recipesDataBaseUpdateProcessorByRecipeOpenApi();

    String requestRecipeLists(int startIndex, int endIndex) throws MalformedURLException;

    void save(Recipes recipe);

    void delete(Recipes recipe);
}
