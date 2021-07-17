package com.example.backend.controllers;

import com.example.backend.services.FoodOpenApiServiceImpl;
import com.example.backend.services.interfaces.RecipeOpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
@RequestMapping("open-api")
@RequiredArgsConstructor
public class OpenApiController {

    private final FoodOpenApiServiceImpl foodOpenApiService;

    private final RecipeOpenApiService recipeOpenApiService;

    @Scheduled(cron = "0 0 4 * * *")
    public void dataUpdateScheduler() {
        foodOpenApiService.nutrientsDataBaseUpdateProcessorByFoodOpenApi();
        recipeOpenApiService.recipesDataBaseUpdateProcessorByRecipeOpenApi();
    }
}
