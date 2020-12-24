package com.example.backend.services.interfaces;

import com.example.backend.models.Recipes;

import java.net.MalformedURLException;
import java.util.ArrayList;

public interface RecipeOpenApiService {
    void recipesDataBaseUpdateProcessorByRecipeOpenApi();

    String requestRecipeLists(int startIndex, int endIndex) throws MalformedURLException;

    double validation(String data);

    ArrayList<String> manualBuilder(String target);

    boolean exceptDuplicatedData(String name);

    void save(Recipes recipe);

    void delete(Recipes recipe);
}
