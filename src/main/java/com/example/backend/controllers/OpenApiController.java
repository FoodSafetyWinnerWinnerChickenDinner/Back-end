package com.example.backend.controllers;

import com.example.backend.services.FoodOpenApi;
import com.example.backend.services.RecipeOpenApi;
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

    private final RecipeOpenApi recipeOpenApi;

    private final FoodOpenApi foodOpenApi;

    @Scheduled(cron = "0 56 9 * * *")
    public void dataUpdateScheduler() {
        recipeOpenApi.updateByOpenApiData();
//        foodOpenApi.updateByOpenApiData();
    }
}
