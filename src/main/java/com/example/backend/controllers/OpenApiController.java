package com.example.backend.controllers;

import com.example.backend.services.FoodOpenApi;
import com.example.backend.services.RecipeOpenApiServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
@RequestMapping("open-api")
@RequiredArgsConstructor
public class OpenApiController {

    private final RecipeOpenApiServiceImpl recipeOpenApiService;

    private final FoodOpenApi foodOpenApi;

    @Scheduled(cron = "0 0 4 * * *")
    public void dataUpdateScheduler() {
        foodOpenApi.updateByOpenApiData();
//        recipeOpenApiService.recipesDataBaseUpdateProcessorByRecipeOpenApi();
    }
}
