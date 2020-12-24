package com.example.backend.controllers;

import com.example.backend.models.Recipes;
import com.example.backend.services.RecipeServiceImpl;
import com.example.backend.services.interfaces.RecipeOpenApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("recipes")
public class RecipeController {
    private final RecipeServiceImpl recipeServiceImpl;
    private final RecipeOpenApiService recipeOpenApiService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    public RecipeController(RecipeServiceImpl recipeServiceImpl, RecipeOpenApiService recipeOpenApiService) {
        this.recipeServiceImpl = recipeServiceImpl;
        this.recipeOpenApiService = recipeOpenApiService;
    }

    @PostMapping("/recommend")
    public ResponseEntity<List<Optional>> menuRecommender(@RequestBody Map<String, Double> ate) {
        recipeOpenApiService.recipesDataBaseUpdateProcessorByRecipeOpenApi();
        List<Optional> recommend = null;

        List<Recipes> recipeList = recipeServiceImpl.recipeListExtractFromDB();

        try {
            double[] ingested = {ate.get("Carbohydrate"), ate.get("Protein"), ate.get("Fat")};
            double size = ate.get("Lists");
            recommend = recipeServiceImpl.termFrequencyInverseDocumentFrequency(ingested, recipeList, size);
        }
        catch (NullPointerException nullPointerException) {
            LOGGER.error(">>> FoodController >> exception >> ", nullPointerException);
            nullPointerException.printStackTrace();
        }

        return new ResponseEntity(recommend, HttpStatus.OK);
    }
}
